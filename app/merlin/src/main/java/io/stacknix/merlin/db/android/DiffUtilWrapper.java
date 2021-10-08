package io.stacknix.merlin.db.android;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import io.stacknix.merlin.db.MerlinObject;
import io.stacknix.merlin.db.MerlinResult;


public class DiffUtilWrapper extends DiffUtil.Callback {

    private final MerlinResult<MerlinObject> oldResult;
    private final MerlinResult<MerlinObject> newResult;


    public DiffUtilWrapper(MerlinResult<MerlinObject> oldResult, MerlinResult<MerlinObject> newResult) {
        this.oldResult = oldResult;
        this.newResult = newResult;
    }

    @Override
    public int getOldListSize() {
        return oldResult.size();
    }

    @Override
    public int getNewListSize() {
        return newResult.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldResult.get(oldItemPosition).areItemsTheSame(newResult.get(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldResult.get(oldItemPosition).areContentsTheSame(newResult.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}