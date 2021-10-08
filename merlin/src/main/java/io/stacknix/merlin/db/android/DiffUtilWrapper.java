package io.stacknix.merlin.db.android;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import org.jetbrains.annotations.NotNull;

import io.stacknix.merlin.db.MerlinObject;
import io.stacknix.merlin.db.MerlinResult;


public class DiffUtilWrapper extends DiffUtil.Callback {

    private final MerlinResult<? extends MerlinObject> oldResult;
    private final MerlinResult<? extends MerlinObject> newResult;


    public static DiffUtil.@NotNull DiffResult
    calculate(MerlinResult<? extends MerlinObject> oldResult, MerlinResult<? extends MerlinObject> newResult) {
        return DiffUtil.calculateDiff(new DiffUtilWrapper(oldResult, newResult), true);
    }

    public DiffUtilWrapper(MerlinResult<? extends MerlinObject> oldResult,
                           MerlinResult<? extends MerlinObject> newResult) {
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