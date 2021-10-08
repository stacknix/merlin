package io.stacknix.merlin.db;

import org.jetbrains.annotations.NotNull;

import io.stacknix.merlin.db.annotations.PrimaryKey;

public class MerlinObject extends BaseModel {
    @PrimaryKey
    public String uuid;


    @Override
    public boolean areItemsTheSame(@NotNull MerlinObject subject) {
        return uuid.equals(subject.uuid);
    }

    @Override
    public boolean areContentsTheSame(MerlinObject subject) {
        return areItemsTheSame(subject);
    }
}
