package io.stacknix.merlin.db;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

import io.stacknix.merlin.db.android.Logging;

public class MerlinResult<T extends MerlinObject> extends ArrayList<T> {

    private static final String TAG = "MerlinResult";

    private final MerlinQuery<T> query;
    private ResultChangeListener<T> resultChangeListener;
    private ObjectChangeListener<T> objectChangeListener;

    public MerlinResult(MerlinQuery<T> query) {
        this.query = query;
    }

    public void listen(ResultChangeListener<T> listener) {
        this.resultChangeListener = listener;
        Merlin.getInstance().listen((tClass, operation) -> {
            if (tClass == query.getObjectClass()) {
                dispatchResult(query.find());
            }
        });
    }

    public void listenObject(ObjectChangeListener<T> listener) {
        this.objectChangeListener = listener;
        Merlin.getInstance().listen((tClass, operation) -> {
            if (tClass == query.getObjectClass()) {
                dispatchResult(query.find());
            }
        });
    }

    private void dispatchResult(MerlinResult<T> result) {
        if (resultChangeListener != null) {
            Logging.i(TAG, "Dispatching Results");
            resultChangeListener.onChange(result);
        } else if (objectChangeListener != null) {
            Logging.i(TAG, "Dispatching Object");
            if (result.isEmpty()) {
                objectChangeListener.onChange(null);
            } else {
                objectChangeListener.onChange(result.get(0));
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void deleteAll() {
        Merlin.getInstance().db().delete(query.getObjectClass(), (List<MerlinObject>) this);
    }

    public MerlinQuery<T> getQuery() {
        return query;
    }
}
