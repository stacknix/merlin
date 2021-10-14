package io.stacknix.merlin.db.commons;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.stacknix.merlin.db.MerlinObject;


public class ResultCompare<T extends MerlinObject> {

    private final List<T> writeData;
    private final List<T> createData;
    private final List<T> deleteData;


    public ResultCompare(@NotNull List<T> localList, @NotNull List<T> remoteList) {
        this.writeData = new ArrayList<>();
        this.createData = new ArrayList<>();
        this.deleteData = new ArrayList<>();

        for (T secondItem : localList) {
            boolean isContain = false;
            for (MerlinObject firstItem : remoteList) {
                if (secondItem.areItemsTheSame(firstItem)) {
                    isContain = true;
                    break;
                }
            }
            if (!isContain) {
                deleteData.add(secondItem);
            }
        }
        for (T firstItem : remoteList) {
            for (MerlinObject secondItem : localList) {
                if (firstItem.areItemsTheSame(secondItem)) {
                    writeData.add(firstItem);
                    break;
                }
            }
        }
        for (T firstItem : remoteList) {
            boolean isContain = false;
            for (MerlinObject secondItem : localList) {
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