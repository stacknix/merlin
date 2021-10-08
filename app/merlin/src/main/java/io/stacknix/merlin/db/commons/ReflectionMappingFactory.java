package io.stacknix.merlin.db.commons;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.stacknix.merlin.db.BaseModel;
import io.stacknix.merlin.db.annotations.Ignore;

public class ReflectionMappingFactory extends MappingFactory<BaseModel> {

    @Override
    public Map<String, Object> getValues(BaseModel subject) throws Exception {
        HashMap<String, Object> valuesMap = new HashMap<>();
        for (Field field : getFields(subject.getClass())) {
            field.setAccessible(true);
            valuesMap.put(field.getName(), field.get(subject));
        }
        return valuesMap;
    }

    @Override
    public void setValues(Map<String, Object> valuesMap, BaseModel subject) {

    }

    @Override
    public BaseModel onCreateInstance(@NotNull Class<BaseModel> modelClass) throws InstantiationException, IllegalAccessException {
        return modelClass.newInstance();
    }

    @Override
    public boolean onCompareObjects(BaseModel first, BaseModel second) throws Exception {
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
        return false;
    }

    @Override
    public String[] getFieldsName(Class<BaseModel> modelClass) {
        List<Field> fields = getFields(modelClass);
        String[] fieldsName = new String[fields.size()];
        for (int i = 0; i < fieldsName.length; i++) {
            fieldsName[0] = fields.get(i).getName();
        }
        return fieldsName;
    }

    /*
     * Exclude static fields.
     * Exclude private fields.
     * Exclude protected fields.
     * Exclude Ignore fields.
     */
    private @NotNull List<Field> getFields(@NotNull Class<?> subjectClass) {
        List<Field> data = new ArrayList<>();
        for (Field field : subjectClass.getFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                if (field.getAnnotation(Ignore.class) == null) {
                    data.add(field);
                }
            }
        }
        return data;
    }
}
