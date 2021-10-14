package io.stacknix.merlin.db;

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
        Merlin.getInstance().listen((tClass, operation) -> {
            if (tClass == query.getObjectClass()) {
                dispatchResult(query.find());
            }
        });
    }

    private void dispatchResult(MerlinResult<T> result) {
        if (listener != null) {
            Logging.i(TAG, "Dispatching Results");
            listener.onChange(result);
        }
    }

    @SuppressWarnings("unchecked")
    public void deleteAll() {
        Merlin.getInstance().db().delete(query.getObjectClass(), (List<MerlinObject>) this);
    }
}
