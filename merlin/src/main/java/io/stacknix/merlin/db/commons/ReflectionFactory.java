package io.stacknix.merlin.db.commons;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import io.stacknix.merlin.db.MappingFactory;
import io.stacknix.merlin.db.MerlinObject;
import io.stacknix.merlin.db.android.Logging;
import io.stacknix.merlin.db.annotations.Ignore;
import io.stacknix.merlin.db.annotations.Internal;

public class ReflectionFactory extends MappingFactory {

    @Override
    public Map<String, Object> getData(MerlinObject subject) {
        Map<String, Object> values = getValues(subject);
        @NotNull List<String> fields = MerlinObject.getFields(subject.getClass());
        Set<String> set = new HashSet<>(fields);
        values.keySet().retainAll(set);
        return values;
    }

    @Override
    public Map<String, Object> getValues(@NotNull MerlinObject subject) {
        Map<String, Object> values = new HashMap<>();
        try {
            for (Field field : getReflectionFields(subject.getClass())) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if (field.getType().isAssignableFrom(int.class)) {
                    values.put(fieldName, field.getInt(subject));
                } else if (field.getType().isAssignableFrom(long.class)) {
                    values.put(fieldName, field.getLong(subject));
                } else if (field.getType().isAssignableFrom(float.class)) {
                    values.put(fieldName, field.getFloat(subject));
                } else if (field.getType().isAssignableFrom(double.class)) {
                    values.put(fieldName, field.getDouble(subject));
                } else if (field.getType().isAssignableFrom(boolean.class)) {
                    values.put(fieldName, field.getBoolean(subject));
                } else if (field.getType().isAssignableFrom(String.class)) {
                    values.put(fieldName, field.get(subject));
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return values;
    }

    @Override
    public Object getValue(@NotNull MerlinObject object, String fieldName) {
        try {
            Field field = object.getClass().getField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void setValues(Map<String, Object> values, @NotNull MerlinObject object) {
        try {
            for (Field field : getReflectionFields(object.getClass())) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object val = values.get(fieldName);
                if (val != null) {
                    if (field.getType().isAssignableFrom(int.class)) {
                        field.setInt(object, (Integer) val);
                    } else if (field.getType().isAssignableFrom(long.class)) {
                        // allow int to long conversion/assignment
                        if (val instanceof Integer) {
                            field.setLong(object, ((Number) val).longValue());
                        } else {
                            field.setLong(object, (Long) val);
                        }
                    } else if (field.getType().isAssignableFrom(float.class)) {
                        field.setFloat(object, (Float) val);
                    } else if (field.getType().isAssignableFrom(double.class)) {
                        // allow float to double conversion/assignment
                        if (val instanceof Float) {
                            field.setDouble(object, ((Number) val).doubleValue());
                        } else {
                            field.setDouble(object, (Double) val);
                        }
                    } else if (field.getType().isAssignableFrom(boolean.class)) {
                        field.setBoolean(object, (Boolean) val);
                    } else if (field.getType().isAssignableFrom(String.class)) {
                        field.set(object, val);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setValue(@NotNull MerlinObject object, String fieldName, Object value) {
        try {
            for (Field field : getReflectionFields(object.getClass())) {
                if (field.getName().equals(fieldName)) {
                    field.setAccessible(true);
                    field.set(object, value);
                    break;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Override
    public <T extends MerlinObject> T onCreateInstance(@NotNull Class<T> tClass) {
        try {
            return tClass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean onCompareObjects(@NotNull MerlinObject first, @NotNull MerlinObject second) {
        Map<String, Object> firstMap = getValues(first);
        Map<String, Object> secondMap = getValues(second);
        for (String key : firstMap.keySet()) {
            if (!Objects.equals(firstMap.get(key), secondMap.get(key))) {
                return false;
            }
        }
        for (String key : secondMap.keySet()) {
            if (!Objects.equals(secondMap.get(key), firstMap.get(key))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public <T extends MerlinObject> FieldInfo[] getFields(Class<T> tClass) {
        List<Field> fields = getReflectionFields(tClass);
        FieldInfo[] fieldsInfo = new FieldInfo[fields.size()];
        for (int i = 0; i < fieldsInfo.length; i++) {
            Field field = fields.get(i);
            fieldsInfo[i] = new FieldInfo(field.getName(), field.getType(), field.isAnnotationPresent(Internal.class));
        }
        return fieldsInfo;
    }


    private @NotNull List<Field> getReflectionFields(@NotNull Class<?> tClass) {
        List<Field> data = new ArrayList<>();
        Field[] allFields = Utils.mergeArray(tClass.getDeclaredFields(), Objects.requireNonNull(tClass.getSuperclass()).getDeclaredFields());
        for (Field field : allFields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                if (!field.isAnnotationPresent(Ignore.class)) {
                    data.add(field);
                }
            }
        }
        return data;
    }

}
