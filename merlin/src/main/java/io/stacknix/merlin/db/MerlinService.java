package io.stacknix.merlin.db;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.stacknix.merlin.db.android.Logging;
import io.stacknix.merlin.db.commons.Flag;
import io.stacknix.merlin.db.commons.RecordNotFound;

@SuppressWarnings({"UnusedReturnValue", "unused", "unchecked"})
public abstract class MerlinService<T extends MerlinObject> {

    private final Class<T> tClass;

    public MerlinService(Class<T> tClass) {
        this.tClass = tClass;
    }

    public abstract T onCreate(Context context, T object) throws Exception;

    public abstract T onWrite(Context context, T object) throws Exception;

    public abstract boolean onUnlink(Context context, T object) throws Exception;

    public abstract T onRead(Context context, String pk) throws Exception;

    public abstract List<T> onSearch(Context context, MerlinQuery<T> query) throws Exception;

    public Class<T> getObjectClass() {
        return tClass;
    }

    public synchronized void synchronize(Context context) throws Exception {
        final String TAG = "MerlinService";
        for (T item : Merlin.where(tClass).find()) {
            switch (item._flag) {
                case Flag.NEED_CREATE:
                    T newItem = onCreate(context, item);
                    newItem._flag = Flag.IDLE;
                    Logging.i(TAG, "Updating local flag to idle.[01]");
                    newItem.save();
                    break;
                case Flag.NEED_WRITE:
                    try {
                        onWrite(context, item);
                        item._flag = Flag.IDLE;
                        Logging.i(TAG, "Updating local flag to idle.[11]");
                        item.save();
                    } catch (RecordNotFound e) {
                        Logging.w(TAG, e.message);
                        Logging.i(TAG, "Deleting from local.[11]");
                        item.delete();
                    }
                case Flag.NEED_UNLINK:
                    try {
                        onUnlink(context, item);
                        Logging.i(TAG, "Deleting from local.[21]");
                        item.delete();
                    } catch (RecordNotFound e) {
                        Logging.w(TAG, e.message);
                        Logging.i(TAG, "Deleting from local.[22]");
                        item.delete();
                    }
                    break;
            }
        }
    }

    private void localize(@NotNull T item, @NotNull T cacheItem) {
        item._timestamp = cacheItem._timestamp;
        item._flag = cacheItem._flag;
    }

    public void performRead(Context context, String pk) throws Exception {
        T item = onRead(context, pk);
        DBAdapter<?> db = Merlin.getInstance().db();
        T cacheItem = db.read(tClass, item.getPrimaryValue());
        if (cacheItem != null) {
            localize(item, cacheItem);
            db.write(tClass, Collections.singletonList(item));
        } else {
            db.create(tClass, Collections.singletonList(item));
        }
    }

    public void performSearch(Context context, MerlinQuery<T> query) throws Exception {
        List<T> items = onSearch(context, query);
        List<T> toCreate = new ArrayList<>();
        List<T> toWrite = new ArrayList<>();
        DBAdapter<?> db = Merlin.getInstance().db();
        for (T item : items) {
            T cacheItem = db.read(tClass, item.getPrimaryValue());
            if (cacheItem != null) {
                localize(item, cacheItem);
                toWrite.add(item);
            } else {
                toCreate.add(item);
            }
        }
        db.write(tClass, (List<MerlinObject>) toWrite);
        db.create(tClass, (List<MerlinObject>) toCreate);
    }

}
