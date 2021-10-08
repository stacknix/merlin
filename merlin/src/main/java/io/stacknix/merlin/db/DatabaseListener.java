package io.stacknix.merlin.db;


public interface DatabaseListener {
     void onChange(Class<? extends MerlinObject> tClass, DBOperation operation);
}
