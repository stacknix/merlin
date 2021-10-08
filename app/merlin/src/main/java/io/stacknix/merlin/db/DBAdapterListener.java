package io.stacknix.merlin.db;


public interface DBAdapterListener {
     void onChange(Class<? extends MerlinObject> tClass);
}
