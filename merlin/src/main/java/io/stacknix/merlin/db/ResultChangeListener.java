package io.stacknix.merlin.db;


public interface ResultChangeListener<T extends MerlinObject> {
     void onChange(MerlinResult<T> data);
}
