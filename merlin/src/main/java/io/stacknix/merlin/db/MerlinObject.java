package io.stacknix.merlin.db;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Collections;
import io.stacknix.merlin.db.android.Logging;
import io.stacknix.merlin.db.annotations.Internal;
import io.stacknix.merlin.db.annotations.Model;
import io.stacknix.merlin.db.annotations.Order;
import io.stacknix.merlin.db.annotations.PrimaryKey;
import io.stacknix.merlin.db.annotations.SortKey;
import io.stacknix.merlin.db.commons.Pair;

public abstract class MerlinObject {
    @PrimaryKey
    public String uuid;

    @Internal
    @SortKey(Order.DESC)
    public long _timestamp;

    @Internal
    public int _flag;

    public static <T extends MerlinObject> @NotNull String getModelName(@NotNull Class<T> tClass) {
        Model modelName = tClass.getAnnotation(Model.class);
        assert modelName != null;
        return modelName.value();
    }

    public static <T extends MerlinObject> @NotNull String getTableName(@NotNull Class<T> tClass) {
        return getModelName(tClass).replaceAll("\\.", "_");
    }

    public static <T extends MerlinObject> @NotNull String getPrimaryKey(@NotNull Class<T> tClass) {
        for (Field field : tClass.getFields()) {
            if (field.getAnnotation(PrimaryKey.class) != null) {
                return field.getName();
            }
        }
        assert false;
        return null;
    }

    public static <T extends MerlinObject> @NotNull Pair<String, Order> getSortKey(@NotNull Class<T> tClass) {
        for (Field field : tClass.getFields()) {
            SortKey sortKey = field.getAnnotation(SortKey.class);
            if (sortKey != null) {
                return new Pair<>(field.getName(), sortKey.value());
            }
        }
        assert false;
        return null;
    }

    public String getPrimaryValue() {
        return (String) getFactory().getValue(this, getPrimaryKey(getClass()));
    }

    public boolean areItemsTheSame(@NotNull MerlinObject subject) {
        return getPrimaryValue().equals(subject.getPrimaryValue());
    }

    public boolean areContentsTheSame(MerlinObject subject) {
       return Merlin.getInstance().getMappingFactory().onCompareObjects(this, subject);
    }

    private boolean isManaged() {
        return getPrimaryValue() != null;
    }

    public void save() {
        DBAdapter<?> db = Merlin.getInstance().db();
        if (!isManaged()) {
            db.create(getClass(), Collections.singletonList(this));
        } else {
            db.write(getClass(), Collections.singletonList(this));
        }
    }

    public void delete() {
        if (isManaged()) {
            DBAdapter<?> db = Merlin.getInstance().db();
            db.delete(getClass(), Collections.singletonList(this));
        }
    }

    private MappingFactory getFactory(){
        return Merlin.getInstance().getMappingFactory();
    }

}
