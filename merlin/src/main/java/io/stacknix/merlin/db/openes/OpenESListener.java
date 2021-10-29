package io.stacknix.merlin.db.openes;


import io.stacknix.merlin.db.MerlinObject;

public interface OpenESListener {
    void onCreate(Class<? extends MerlinObject> tClass, String model, String uuid, MerlinObject data);
    void onWrite(Class<? extends MerlinObject> tClass, String model, String uuid, MerlinObject data);
    void onUnlink(Class<? extends MerlinObject> tClass, String model, String uuid);
}
