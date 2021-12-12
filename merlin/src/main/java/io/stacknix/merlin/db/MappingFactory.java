package io.stacknix.merlin.db;

import java.util.Map;

import io.stacknix.merlin.db.commons.FieldInfo;

public abstract class MappingFactory {
    /**
     * It returns serialize data, Also it ignores local fields
     */
    public abstract Map<String, Object> getData(MerlinObject object);

    /**
     * It returns all the values included internal.
     */
    public abstract Map<String, Object> getValues(MerlinObject object);

    public abstract Object getValue(MerlinObject object, String fieldName);

    public abstract void setValues(Map<String, Object> values, MerlinObject object);

    public abstract void setValue(MerlinObject object, String fieldName, Object value);

    public abstract <T extends MerlinObject> T onCreateInstance(Class<T> tClass);

    public abstract boolean onCompareObjects(MerlinObject first, MerlinObject second);

    public abstract <T extends MerlinObject> FieldInfo[] getFields(Class<T> tClass);
}
