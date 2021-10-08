package io.stacknix.merlin.db.android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDBHelper extends SQLiteOpenHelper {

    private final String[] createTableQueries;
    private final String[] deleteTableQueries;

    public SQLiteDBHelper(Context context, String databaseName, int databaseVersion,  String[] createTableQueries, String[] deleteTableQueries) {
        super(context, databaseName, null, databaseVersion);
        this.createTableQueries = createTableQueries;
        this.deleteTableQueries = deleteTableQueries;
    }

    public void onCreate(SQLiteDatabase db) {
        for (String query : createTableQueries) {
            db.execSQL(query);
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (String query : deleteTableQueries) {
            db.execSQL(query);
        }
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
