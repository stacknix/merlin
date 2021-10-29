package io.stacknix.merlin.db;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.stacknix.merlin.db.android.Logging;
import io.stacknix.merlin.db.annotations.Internal;
import io.stacknix.merlin.db.annotations.Model;
import io.stacknix.merlin.db.annotations.PrimaryKey;
import io.stacknix.merlin.db.commons.FieldInfo;
import io.stacknix.merlin.db.commons.MerlinException;

public abstract class MerlinObject {

    public static final String TAG = "MerlinObject";

    @Internal
    private int _flag;

    public static <T extends MerlinObject> @NotNull String getModelName(@NotNull Class<T> tClass) {
        Model modelName = tClass.getAnnotation(Model.class);
        assert modelName != null;
        return modelName.value();
    }

    public static <T extends MerlinObject> @NotNull String getPrimaryKey(@NotNull Class<T> tClass) {
        for (Field field : tClass.getFields()) {
            if (field.getAnnotation(PrimaryKey.class) != null) {
                return field.getName();
            }
        }
        throw new MerlinException(String.format("Primary key not defined within %s", getModelName(tClass)));
    }

    public static <T extends MerlinObject> @NotNull String getTableName(@NotNull Class<T> tClass) {
        return getModelName(tClass).replaceAll("\\.", "_");
    }

    public static <T extends MerlinObject> @NotNull List<String> getFields(Class<T> tClass) {
        List<String> fields = new ArrayList<>();
        for (FieldInfo info : getFactory().getFields(tClass)) {
            if (!info.isInternal()) {
                fields.add(info.getName());
            }
        }
        return fields;
    }

    @Deprecated
    public static <T extends MerlinObject> @NotNull Map<String, Object> getValues(Class<T> tClass, MerlinObject object) {
        Map<String, Object> values = new HashMap<>();
        for (FieldInfo info : getFactory().getFields(tClass)) {
            if (!info.isInternal()) {
                values.put(info.getName(), getFactory().getValue(object, info.getName()));
            }
        }
        return values;
    }

    public String getPrimaryValue() {
        return (String) getFactory().getValue(this, getPrimaryKey(getClass()));
    }

    public void setPrimaryValue(String value) {
        getFactory().setValue(this, getPrimaryKey(getClass()), value);
    }

    public boolean areItemsTheSame(@NotNull MerlinObject subject) {
        return getPrimaryValue().equals(subject.getPrimaryValue());
    }

    public boolean areContentsTheSame(MerlinObject subject) {
        return getFactory().onCompareObjects(this, subject);
    }

    public void save() {
        DBAdapter<?> db = Merlin.getInstance().db();
        if (db.read(getClass(), getPrimaryValue()) == null) {
            Logging.i(TAG, "save", "create");
            db.create(getClass(), Collections.singletonList(this));
        } else {
            Logging.i(TAG, "save", "write");
            db.write(getClass(), Collections.singletonList(this));
        }
    }

    public void delete() {
        if (getPrimaryValue() != null) {
            DBAdapter<?> db = Merlin.getInstance().db();
            db.delete(getClass(), Collections.singletonList(this));
        }
    }

    private static MappingFactory getFactory() {
        return Merlin.getInstance().getMappingFactory();
    }

    public int getFlag() {
        return this._flag;
    }

    public void setFlag(int flag) {
        this._flag = flag;
    }


}





