package io.stacknix.merlin.db.commons;

import java.util.Map;

public abstract class MappingFactory<T> {
    public abstract Map<String, Object> getValues(T subject) throws Exception;
    public abstract void setValues(Map<String, Object> valuesMap, T subject) throws Exception;
    public abstract T onCreateInstance(Class<T> tClass) throws Exception;
    public abstract boolean onCompareObjects(T first, T second) throws Exception;
    public abstract String[] getFieldsName(Class<T> tClass) throws Exception;
}
