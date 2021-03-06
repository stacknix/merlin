package io.stacknix.merlin.db.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import io.stacknix.merlin.db.DBAdapter;
import io.stacknix.merlin.db.DBOperation;
import io.stacknix.merlin.db.DatabaseListener;
import io.stacknix.merlin.db.MappingFactory;
import io.stacknix.merlin.db.Merlin;
import io.stacknix.merlin.db.MerlinObject;
import io.stacknix.merlin.db.MerlinQuery;
import io.stacknix.merlin.db.MerlinResult;
import io.stacknix.merlin.db.annotations.Order;
import io.stacknix.merlin.db.commons.FieldInfo;
import io.stacknix.merlin.db.commons.MerlinException;
import io.stacknix.merlin.db.commons.Pair;
import io.stacknix.merlin.db.queries.SQLBuilder;

public class SQLiteAdapter extends DBAdapter<SQLiteDatabase> {

    private static final String TAG = "SQLiteAdapter";
    private static final int SQL_MAX_ARGUMENTS = 999;
    private final Context context;
    private final MappingFactory factory;
    private final DatabaseListener listener;

    public SQLiteAdapter(Context context, MappingFactory factory, DatabaseListener listener) {
        this.context = context;
        this.factory = factory;
        this.listener = listener;
    }

    @Override
    protected <T extends MerlinObject> void onCreate(Class<T> tClass, @NotNull List<MerlinObject> objects) {
        String tableName = MerlinObject.getTableName(tClass);
        for (MerlinObject item : objects) {
            if (item.getPrimaryValue() == null) {
                throw new MerlinException("Primary key value not given.");
            }
            ContentValues values = Utils.mapToContentValues(factory.getValues(item));
            Logging.i(TAG, values);
            long insertIndex = getDatabase().insertOrThrow(tableName, null, values);
            Logging.i(TAG, insertIndex);
        }
        listener.onChange(tClass, DBOperation.create);
    }

    @Override
    protected <T extends MerlinObject> void onWrite(Class<T> tClass, @NotNull List<MerlinObject> objects) {
        String tableName = MerlinObject.getTableName(tClass);
        String primaryKey = MerlinObject.getPrimaryKey(tClass);
        for (MerlinObject item : objects) {
            String selection = String.format("%s = ?", primaryKey);
            String[] selectionArgs = {item.getPrimaryValue()};
            ContentValues values = Utils.mapToContentValues(factory.getValues(item));
            getDatabase().update(tableName, values, selection, selectionArgs);
        }
        listener.onChange(tClass, DBOperation.write);
    }

    @Override
    protected <T extends MerlinObject> void onDelete(Class<T> tClass, List<MerlinObject> objects) {
        String tableName = MerlinObject.getTableName(tClass);
        for (List<MerlinObject> object : Lists.partition(objects, SQL_MAX_ARGUMENTS)) {
            String sqlArgs = Joiner.on(", ").join(Collections.nCopies(object.size(), "?"));
            String selection = String.format("%s IN (%s)", MerlinObject.getPrimaryKey(tClass), sqlArgs);
            String[] selectionArgs = new String[object.size()];
            for (int i = 0; i < object.size(); i++) {
                selectionArgs[i] = String.valueOf(object.get(i).getPrimaryValue());
            }
            getDatabase().delete(tableName, selection, selectionArgs);
        }
        listener.onChange(tClass, DBOperation.delete);
    }

    @Override
    protected <T extends MerlinObject> T onRead(Class<T> tClass, String pk) {
        T instance = null;
        String tableName = MerlinObject.getTableName(tClass);
        String primaryKey = MerlinObject.getPrimaryKey(tClass);
        String selection = String.format("%s = ?", primaryKey);
        @NotNull Pair<String, Order> sorting = MerlinQuery.getDefaultSorting(tClass);
        String sortOrder = String.format("%s %s", sorting.first, sorting.second.name());
        String[] selectionArgs = {pk};
        Cursor cursor = getDatabase().query(tableName, null, selection, selectionArgs, null, null, sortOrder);
        FieldInfo[] fieldInfo = factory.getFields(tClass);
        if (cursor.moveToNext()) {
            instance = factory.onCreateInstance(tClass);
            factory.setValues(Utils.cursorToMap(fieldInfo, cursor), instance);
        }
        cursor.close();
        return instance;
    }

    @Override
    protected <T extends MerlinObject> MerlinResult<T> onSearch(Class<T> tClass, @NotNull MerlinQuery<T> query) {
        String tableName = MerlinObject.getTableName(tClass);
        @NotNull Pair<String, Order> sorting = query.getSorting();
        String sortOrder = String.format("%s %s", sorting.first, sorting.second.name());
        SQLBuilder sb = new SQLBuilder(query);
        Cursor cursor;
        if (sb.getSQL() == null) {
            cursor = getDatabase().query(tableName, null, null, null, null, null, sortOrder);
        } else {
            String simple = String.format("SELECT * FROM %s WHERE %s ORDER BY %s LIMIT %s", tableName, sb.getSQL(), sortOrder, query.getLimit());
            Logging.i(TAG, "sql:", simple);
            Logging.i(TAG, "args:", new Gson().toJson(sb.getSelectionArgs()));
            cursor = getDatabase().rawQuery(simple, sb.getSelectionArgs());
        }
        MerlinResult<T> data = new MerlinResult<>(query);
        FieldInfo[] fieldInfo = factory.getFields(tClass);
        while (cursor.moveToNext()) {
            T instance = factory.onCreateInstance(tClass);
            factory.setValues(Utils.cursorToMap(fieldInfo, cursor), instance);
            data.add(instance);
        }
        cursor.close();
        return data;
    }

    @Override
    protected SQLiteDatabase onConnectDatabase() {
        return getWritableDatabase();
    }

    @Override
    protected void onDisconnectDatabase(@NotNull SQLiteDatabase database) {
        database.close();
    }

    private SQLiteDatabase getWritableDatabase() {
        List<Class<? extends MerlinObject>> models = Merlin.getInstance().getModels();
        List<String> createTableSQList = getCreateTableQueries(models);
        List<String> deleteTableSQList = getDeleteTableQueries(models);
        int dbVersion = getCheapDBVersion(createTableSQList);
        SQLiteDBHelper dbHelper = new SQLiteDBHelper(context, Merlin.DEFAULT_DATABASE_NAME, dbVersion,
                createTableSQList.toArray(new String[0]),
                deleteTableSQList.toArray(new String[0]));
        return dbHelper.getWritableDatabase();
    }

    private @NotNull List<String> getCreateTableQueries(@NotNull List<Class<? extends MerlinObject>> models) {
        List<String> data = new ArrayList<>();
        for (Class<? extends MerlinObject> tClass : models) {
            String tableName = MerlinObject.getTableName(tClass);
            String primaryKey = MerlinObject.getPrimaryKey(tClass);
            List<String> columns = new ArrayList<>();
            columns.add(String.format("%s TEXT PRIMARY KEY NOT NULL", primaryKey));
            FieldInfo[] fieldList = factory.getFields(tClass);
            for (FieldInfo field : fieldList) {
                String fieldName = field.getName();
                if (!fieldName.equals(primaryKey)) {
                    Class<?> type = field.getType();
                    if (type == int.class || type == long.class || type == boolean.class) {
                        columns.add(String.format("%s INTEGER", fieldName));
                    } else if (type == float.class || type == double.class) {
                        columns.add(String.format("%s REAL", fieldName));
                    } else if (type == String.class) {
                        columns.add(String.format("%s TEXT", fieldName));
                    }
                }
            }
            String createTableSQL = Joiner.on(", ").join(columns);
            data.add(String.format("CREATE TABLE %s (%s)", tableName, createTableSQL));
        }
        return data;
    }

    private @NotNull List<String> getDeleteTableQueries(@NotNull List<Class<? extends MerlinObject>> models) {
        List<String> data = new ArrayList<>();
        for (Class<? extends MerlinObject> tClass : models) {
            String tableName = MerlinObject.getTableName(tClass);
            data.add(String.format("DROP TABLE IF EXISTS %s", tableName));
        }
        return data;
    }

    private int getCheapDBVersion(List<String> createTableSQList) {
        Preferences pref = new Preferences(context);
        String TAG = "SQLITE_SCHEMA_HASH";
        int dbVersion = Settings.getDBVersion(context);
        int oldHash = pref.getInt(TAG, 0);
        int newHash = Joiner.on("").join(createTableSQList).hashCode();
        if (oldHash != newHash) {
            dbVersion++;
            Settings.setDBVersion(context, dbVersion);
            pref.setInt(TAG, newHash);
        }
        return dbVersion;
    }


}
