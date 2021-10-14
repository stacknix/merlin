package io.stacknix.merlin.db;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.stacknix.merlin.db.commons.Flag;

public abstract class DBAdapter<DB> {

    private DB database;

    protected abstract <T extends MerlinObject> void onCreate(Class<T> tClass, List<MerlinObject> objects);

    protected abstract <T extends MerlinObject> void onWrite(Class<T> tClass, List<MerlinObject> objects);

    protected abstract <T extends MerlinObject> void onDelete(Class<T> tClass, List<MerlinObject> objects);

    protected abstract <T extends MerlinObject> T onRead(Class<T> tClass, String pk) ;

    protected abstract <T extends MerlinObject> MerlinResult<T> onSearch(Class<T> tClass, MerlinQuery<T> query);

    protected abstract DB onConnectDatabase();

    protected abstract void onDisconnectDatabase(DB database);

    protected synchronized DB getDatabase() {
        if (this.database == null) {
            this.database = onConnectDatabase();
        }
        return this.database;
    }

    protected synchronized void close() {
        if (this.database != null) {
            onDisconnectDatabase(this.database);
            this.database = null;
        }
    }

    public <T extends MerlinObject> void create(Class<T> tClass, List<MerlinObject> objects) {
        if(!objects.isEmpty()){
            onCreate(tClass, objects);
        }
    }

    public <T extends MerlinObject> void write(Class<T> tClass, @NotNull List<MerlinObject> objects) {
        if(!objects.isEmpty()) {
            onWrite(tClass, objects);
        }
    }

    public <T extends MerlinObject> void delete(Class<T> tClass, @NotNull List<MerlinObject> objects) {
        if(!objects.isEmpty()) {
            onDelete(tClass, objects);
        }
    }

    public <T extends MerlinObject> T read(Class<T> tClass, String pk) {
        return onRead(tClass, pk);
    }

    public <T extends MerlinObject> MerlinResult<T> search(MerlinQuery<T> query) {
        return onSearch(query.getObjectClass(), query);
    }

}
