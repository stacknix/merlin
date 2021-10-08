package io.stacknix.merlin.db;

import org.jetbrains.annotations.NotNull;

import io.stacknix.merlin.db.Merlin;
import io.stacknix.merlin.db.MerlinObject;

public abstract class BaseModel {

    public abstract boolean areItemsTheSame(@NotNull MerlinObject subject);

    public abstract boolean areContentsTheSame(MerlinObject subject);

}
