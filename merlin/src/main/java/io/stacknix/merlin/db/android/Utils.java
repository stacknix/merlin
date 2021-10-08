package io.stacknix.merlin.db.android;

import android.content.ContentValues;
import android.database.Cursor;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import io.stacknix.merlin.db.commons.FieldInfo;

public class Utils {

    public static @NotNull ContentValues mapToContentValues(@NotNull Map<String, Object> map) {
        ContentValues contentValues = new ContentValues();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value == null) {
                contentValues.putNull(key);
                continue;
            }
            if (value instanceof Integer) {
                contentValues.put(key, (Integer) value);
            } else if (value instanceof Long) {
                contentValues.put(key, (Long) value);
            } else if (value instanceof Float) {
                contentValues.put(key, (Float) value);
            } else if (value instanceof Double) {
                contentValues.put(key, (Double) value);
            } else if (value instanceof Boolean) {
                contentValues.put(key, (Boolean) value);
            } else if (value instanceof String) {
                contentValues.put(key, (String) value);
            } else if (value instanceof Short) {
                contentValues.put(key, (Short) value);
            } else if (value instanceof byte[]) {
                contentValues.put(key, (byte[]) value);
            } else if (value instanceof Byte) {
                contentValues.put(key, (Byte) value);
            }
        }
        return contentValues;
    }


    public static  @NotNull Map<String, Object> cursorToMap(@NotNull FieldInfo[] fields, Cursor cursor) {
        Map<String, Object> values = new HashMap<>();
        for (FieldInfo field : fields) {
            String key = field.getName();
            if (field.getType().isAssignableFrom(int.class)) {
                values.put(key, cursor.getInt(cursor.getColumnIndexOrThrow(key)));
            } else if (field.getType().isAssignableFrom(long.class)) {
                values.put(key, cursor.getLong(cursor.getColumnIndexOrThrow(key)));
            } else if (field.getType().isAssignableFrom(float.class)) {
                values.put(key, cursor.getFloat(cursor.getColumnIndexOrThrow(key)));
            } else if (field.getType().isAssignableFrom(double.class)) {
                values.put(key, cursor.getDouble(cursor.getColumnIndexOrThrow(key)));
            } else if (field.getType().isAssignableFrom(boolean.class)) {
                values.put(key, cursor.getInt(cursor.getColumnIndexOrThrow(key)) > 0);
            } else if (field.getType().isAssignableFrom(String.class)) {
                values.put(key, cursor.getString(cursor.getColumnIndexOrThrow(key)));
            } else if (field.getType().isAssignableFrom(byte[].class)) {
                values.put(key, cursor.getBlob(cursor.getColumnIndexOrThrow(key)));
            } else if (field.getType().isAssignableFrom(Integer.class)) {
                values.put(key, cursor.getInt(cursor.getColumnIndexOrThrow(key)));
            } else if (field.getType().isAssignableFrom(Long.class)) {
                values.put(key, cursor.getLong(cursor.getColumnIndexOrThrow(key)));
            } else if (field.getType().isAssignableFrom(Float.class)) {
                values.put(key, cursor.getFloat(cursor.getColumnIndexOrThrow(key)));
            } else if (field.getType().isAssignableFrom(Double.class)) {
                values.put(key, cursor.getDouble(cursor.getColumnIndexOrThrow(key)));
            } else if (field.getType().isAssignableFrom(Boolean.class)) {
                values.put(key, cursor.getInt(cursor.getColumnIndexOrThrow(key)) > 0);
            } else if (field.getType().isAssignableFrom(Byte[].class)) {
                values.put(key, cursor.getBlob(cursor.getColumnIndexOrThrow(key)));
            }
        }
        return values;
    }

}
