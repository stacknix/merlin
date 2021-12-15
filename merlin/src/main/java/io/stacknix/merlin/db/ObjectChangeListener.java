package io.stacknix.merlin.db;

import org.jetbrains.annotations.Nullable;

public interface ObjectChangeListener <T extends MerlinObject> {
    void onChange(@Nullable T data);
}
