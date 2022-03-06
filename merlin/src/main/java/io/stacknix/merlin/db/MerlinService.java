package io.stacknix.merlin.db;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

import io.stacknix.merlin.db.commons.Flag;
import io.stacknix.merlin.db.commons.RecordNotFound;
import io.stacknix.merlin.db.commons.ResultCompare;
import io.stacknix.merlin.db.networking.HttpScheme;
import io.stacknix.merlin.db.networking.Requests;

public abstract class MerlinService<T extends MerlinObject> {

    private final Class<T> tClass;

    public MerlinService(Class<T> tClass) {
        this.tClass = tClass;
    }

    public Class<T> getObjectClass() {
        return tClass;
    }

    public abstract T onCreate(T object) throws Exception;

    public abstract T onUpdate(T object) throws Exception;

    public abstract T onRead(long id) throws Exception;

    public abstract List<T> onRead(MerlinQuery<T> query) throws Exception;

    public abstract void onDelete(T object) throws Exception;

    public @NotNull T create(@NotNull T object) throws Exception {
        T item = onCreate(object);
        item.save();
        return item;
    }

    public @NotNull T update(@NotNull T object) throws Exception {
        T item = onUpdate(object);
        item.save();
        return item;
    }

    public @Nullable T read(long id) throws Exception {
        T remoteItem = onRead(id);
        DBAdapter<?> db = Merlin.getInstance().db();
        T localItem = db.read(tClass, remoteItem.getPrimaryValue());
        if (localItem != null) {
            db.write(tClass, Collections.singletonList(remoteItem));
        } else {
            db.create(tClass, Collections.singletonList(remoteItem));
        }
        return remoteItem;
    }

    public @NotNull List<T> read(MerlinQuery<T> query) throws Exception {
        DBAdapter<?> db = Merlin.getInstance().db();
        List<T> localList = db.search(query);
        List<T> remoteList = onRead(query);
        ResultCompare<T> result = new ResultCompare<>(localList, remoteList);
        db.delete(tClass, result.getDeleteData());
        db.write(tClass, result.getWriteData());
        db.create(tClass, result.getCreateData());
        return db.search(query);
    }

    public void delete(@NotNull T object) throws Exception {
        onDelete(object);
        object.delete();
    }

    public synchronized void performSync() throws Exception {
        for (T localResource : Merlin.where(tClass).isNull(MerlinObject.FLAG).find()) {
            if (localResource.getFlag().equals(Flag.CREATE)) {
                T remoteResource = onCreate(localResource);
                remoteResource.clearFlag();
                remoteResource.save();
            } else if (localResource.getFlag().equals(Flag.UPDATE)) {
                try {
                    T remoteResource = onUpdate(localResource);
                    remoteResource.clearFlag();
                    remoteResource.save();
                } catch (RecordNotFound e) {
                    localResource.delete();
                }
            } else if (localResource.getFlag().equals(Flag.DELETE)) {
                try {
                    onDelete(localResource);
                } catch (RecordNotFound ignored) {
                } finally {
                    localResource.delete();
                }
            }
        }
    }
}
