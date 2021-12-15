package io.stacknix.merlin.db;

public interface ObjectChangeListener <T extends MerlinObject> {
    void onChange(T data);
}
