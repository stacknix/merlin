package io.stacknix.merlin.db;

import android.content.Context;

import java.util.Collections;
import java.util.List;

import io.stacknix.merlin.db.commons.ResultCompare;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public abstract class MerlinServiceLegacy<T extends MerlinObject, I> {

    private final Class<T> tClass;

    public MerlinServiceLegacy(Class<T> tClass) {
        this.tClass = tClass;
    }

    public abstract T onCreate(Context context, T object) throws Exception;

    public abstract T onWrite(Context context, T object) throws Exception;

    public abstract boolean onUnlink(Context context, T object) throws Exception;

    public abstract T onRead(Context context, I id) throws Exception;

    public abstract List<T> onSearch(Context context, MerlinQuery<T> query) throws Exception;

    public abstract void onException(Exception exception);

    public Class<T> getObjectClass() {
        return tClass;
    }

    public void read(Context context, I key) {
        try {
            T remoteItem = onRead(context, key);
            DBAdapter<?> db = Merlin.getInstance().db();
            T localItem = db.read(tClass, remoteItem.getPrimaryValue());
            if (localItem != null) {
                db.write(tClass, Collections.singletonList(remoteItem));
            } else {
                db.create(tClass, Collections.singletonList(remoteItem));
            }
        } catch (Exception e) {
            onException(e);
        }
    }

    public void search(Context context) {
        search(context, new MerlinQuery<>(getObjectClass()));
    }

    public void search(Context context, MerlinQuery<T> query) {
        try {
            DBAdapter<?> db = Merlin.getInstance().db();
            List<T> localList = db.search(query);
            List<T> remoteList = onSearch(context, query);
            ResultCompare<T> result = new ResultCompare<>(localList, remoteList);
            db.delete(tClass, result.getDeleteData());
            db.write(tClass, result.getWriteData());
            db.create(tClass, result.getCreateData());
        } catch (Exception e) {
            onException(e);
        }
    }

    /**
     * this method sync existing record CREATE | WRITE | UNLINK
     *
     * @param context
     * @throws Exception
     */
    public synchronized void synchronize(Context context) {

    }

}
