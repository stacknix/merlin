package io.stacknix.merlin.db;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.stacknix.merlin.db.android.SQLiteAdapter;
import io.stacknix.merlin.db.commons.ReflectionFactory;

public class Merlin {

    private static Merlin instance;
    public static final String DEFAULT_DATABASE_NAME = "merlin.db";

    private final List<Class<? extends MerlinObject>> models;
    private final DBAdapter<?> db;
    private final List<DatabaseListener> listeners;
    private final MappingFactory factory;

    public static synchronized Merlin getInstance() {
        if (instance == null) {
            throw new RuntimeException("Attempt to access objects while Merlin database is not initialized.");
        }
        return instance;
    }

    public static synchronized void connect(Context context, List<Class<? extends MerlinObject>> models) {
        if (instance == null) {
            instance = new Merlin(context, models);
        }
    }

    public static synchronized void disconnect() {
        if (instance != null) {
            instance.db.close();
            instance = null;
        }
    }

    private Merlin(Context context, List<Class<? extends MerlinObject>> models) {
        this.models = models;
        this.listeners = new ArrayList<>();
        this.factory = new ReflectionFactory();
        this.db = new SQLiteAdapter(context, factory, (tClass, operation) -> {
            for (DatabaseListener item : listeners) {
                item.onChange(tClass, operation);
            }
        });
    }


    public MappingFactory getMappingFactory() {
        return factory;
    }


    public List<Class<? extends MerlinObject>> getModels() {
        return this.models;
    }

    public Class<? extends MerlinObject> findModel(String modelName) {
        for (Class<? extends MerlinObject> tClass : getModels()) {
            if (MerlinObject.getModelName(tClass).equals(modelName)) {
                return tClass;
            }
        }
        return null;
    }

    public DBAdapter<?> db() {
        return this.db;
    }

    public void listen(DatabaseListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public static <T extends MerlinObject> @NotNull MerlinQuery<T> where(Class<T> tClass) {
        return new MerlinQuery<>(tClass);
    }


}
