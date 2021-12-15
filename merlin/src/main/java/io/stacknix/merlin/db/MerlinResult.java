package io.stacknix.merlin.db;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

import io.stacknix.merlin.db.android.Logging;

public class MerlinResult<T extends MerlinObject> extends ArrayList<T> {

    private static final String TAG = "MerlinResult";

    private final MerlinQuery<T> query;
    private ResultChangeListener<T> listener;

    public MerlinResult(MerlinQuery<T> query) {
        this.query = query;
    }

    public void listen(ResultChangeListener<T> listener) {
        this.listener = listener;
        dispatchResult(query.find(), true);
        Merlin.getInstance().listen((tClass, operation) -> {
            if (tClass == query.getObjectClass()) {
                dispatchResult(query.find(), true);
            }
        });
    }

    public void observe(ResultChangeListener<T> listener) {
        this.listener = listener;
        dispatchResult(query.find(), false);
        Merlin.getInstance().listen((tClass, operation) -> {
            if (tClass == query.getObjectClass()) {
                dispatchResult(query.find(), false);
            }
        });
    }

    private void dispatchResult(MerlinResult<T> result, boolean mainThread) {
        if (listener != null) {
            Logging.i(TAG, "Dispatching Results");
            if (!mainThread) {
                listener.onChange(result);
            } else {
                new Handler(Looper.getMainLooper()).post(() -> listener.onChange(result));
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
