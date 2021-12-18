package io.stacknix.merlin.db;

import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.stacknix.merlin.db.android.Logging;

public class MerlinResult<T extends MerlinObject> extends ArrayList<T> {

    private static final String TAG = "MerlinResult";

    private final MerlinQuery<T> query;
    private ResultChangeListener<T> resultChangeListener;

    public MerlinResult(MerlinQuery<T> query) {
        this.query = query;
    }

    public void observe(ResultChangeListener<T> listener) {
        this.resultChangeListener = listener;
        Merlin.getInstance().listen((tClass, operation) -> {
            if (tClass == query.getObjectClass()) {
                dispatchResult(query.find());
            }
        });
    }

    public @NotNull MutableLiveData<MerlinResult<T>> getLiveData() {
        final MutableLiveData<MerlinResult<T>> liveData = new MutableLiveData<>(this);
        this.observe(liveData::postValue);
        return liveData;
    }

    private void dispatchResult(MerlinResult<T> result) {
        if (resultChangeListener != null) {
            Logging.i(TAG, "Dispatching Results");
            resultChangeListener.onChange(result);
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
