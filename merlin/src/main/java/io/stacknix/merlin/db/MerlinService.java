package io.stacknix.merlin.db;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import io.stacknix.merlin.db.android.Logging;
import io.stacknix.merlin.db.commons.Flag;
import io.stacknix.merlin.db.commons.RecordNotFound;
import io.stacknix.merlin.db.commons.ResultCompare;

@SuppressWarnings({"UnusedReturnValue", "unused"})
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
            switch (item.getFlag()) {
                case Flag.NEED_CREATE:
                    T newItem = onCreate(context, item);
                    newItem.setFlag(Flag.IDLE);
                    Logging.i(TAG, "Updating local flag to idle.[01]");
                    newItem.save();
                    break;
                case Flag.NEED_WRITE:
                    try {
                        onWrite(context, item);
                        item.setFlag(Flag.IDLE);
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

    }

    public void performRead(Context context, String pk) throws Exception {
        T remoteItem = onRead(context, pk);
        DBAdapter<?> db = Merlin.getInstance().db();
        T localItem = db.read(tClass, remoteItem.getPrimaryValue());
        if (localItem != null) {
            db.write(tClass, Collections.singletonList(remoteItem));
        } else {
            db.create(tClass, Collections.singletonList(remoteItem));
        }
    }

    public void performSearch(Context context, MerlinQuery<T> query) throws Exception {
        DBAdapter<?> db = Merlin.getInstance().db();
        List<T> localList = db.search(query);
        List<T> remoteList = onSearch(context, query);
        ResultCompare<T> result = new ResultCompare<>(localList, remoteList);
        db.delete(tClass, result.getDeleteData());
        db.write(tClass, result.getWriteData());
        db.create(tClass, result.getCreateData());
    }

}
