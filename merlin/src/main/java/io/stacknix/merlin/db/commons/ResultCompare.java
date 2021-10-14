package io.stacknix.merlin.db.commons;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.stacknix.merlin.db.MerlinObject;


public class ResultCompare<T extends MerlinObject> {

    private final List<T> writeData;
    private final List<T> createData;
    private final List<T> deleteData;


    public ResultCompare(@NotNull List<T> firstList, @NotNull List<T> secondList) {
        this.writeData = new ArrayList<>();
        this.createData = new ArrayList<>();
        this.deleteData = new ArrayList<>();

        for (T secondItem : secondList) {
            boolean isContain = false;
            for (MerlinObject firstItem : firstList) {
                if (secondItem.areItemsTheSame(firstItem)) {
                    isContain = true;
                    break;
                }
            }
            if (!isContain) {
                deleteData.add(secondItem);
            }
        }
        for (T firstItem : firstList) {
            for (MerlinObject secondItem : secondList) {
                if (firstItem.areItemsTheSame(secondItem)) {
                    writeData.add(firstItem);
                    break;
                }
            }
        }
        for (T firstItem : firstList) {
            boolean isContain = false;
            for (MerlinObject secondItem : secondList) {
                if (firstItem.areItemsTheSame(secondItem)) {
                    isContain = true;
                    break;
                }
            }
            if (!isContain) {
                createData.add(firstItem);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public List<MerlinObject> getCreateData() {
        return (List<MerlinObject>) createData;
    }

    @SuppressWarnings("unchecked")
    public List<MerlinObject> getWriteData() {
        return (List<MerlinObject>) writeData;
    }

    @SuppressWarnings("unchecked")
    public List<MerlinObject> getDeleteData() {
        return (List<MerlinObject>) deleteData;
    }

}