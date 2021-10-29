package io.stacknix.merlin.db.openes;


import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import io.stacknix.merlin.db.MappingFactory;
import io.stacknix.merlin.db.Merlin;
import io.stacknix.merlin.db.MerlinObject;
import io.stacknix.merlin.db.android.Logging;


public class OpenES implements OpenESListener {

    private final HashMap<String, Object> remoteData;
    private final MappingFactory mappingFactory;

    public OpenES(HashMap<String, Object> remoteData) {
        this.remoteData = remoteData;
        this.mappingFactory = Merlin.getInstance().getMappingFactory();
        Logging.e("->", remoteData);
    }

    @SuppressWarnings("unchecked")
    public void commit() {
        String esVersion = (String) remoteData.get("open-es");
        Logging.i("open-es", esVersion);
        List<HashMap<String, Object>> resources = (List<HashMap<String, Object>>) remoteData.get("resources");
        for (int i = 0; i < Objects.requireNonNull(resources).size(); i++) {
            HashMap<String, Object> resource = resources.get(i);
            String model = (String) resource.get("model");
            String event = (String) resource.get("event");
            String uuid = (String) resource.get("uuid");
            Class<? extends MerlinObject> tClass = Merlin.getInstance().findModel(model);
            if (tClass != null) {
                switch (OpenESEvent.valueOf(event)) {
                    case create:
                        MerlinObject createInstance = mappingFactory.onCreateInstance(tClass);
                        mappingFactory.setValues((HashMap<String, Object>) resource.get("data"), createInstance);
                        onCreate(tClass, model, uuid, createInstance);
                        break;
                    case write:
                        MerlinObject writeInstance = mappingFactory.onCreateInstance(tClass);
                        mappingFactory.setValues((HashMap<String, Object>) resource.get("data"), writeInstance);
                        onWrite(tClass, model, uuid, writeInstance);
                        break;
                    case unlink:
                        onUnlink(tClass, model, uuid);
                        break;
                }
            }
        }
    }

    @Override
    public void onUnlink(Class<? extends MerlinObject> tClass, String model, String uuid) {
        MerlinObject localObject = Merlin.where(tClass).equal("uuid", uuid).first();
        if (localObject != null) {
            Merlin.getInstance().db().delete(tClass, Collections.singletonList(localObject));
        }
    }

    @Override
    public void onCreate(Class<? extends MerlinObject> tClass, String model, String uuid, @NotNull MerlinObject data) {
        MerlinObject item = Merlin.where(tClass).equal("uuid", uuid).first();
        data.uuid = uuid;
        if (item != null) {
            Merlin.getInstance().db().write(tClass, Collections.singletonList(data));
        } else {
            Merlin.getInstance().db().create(tClass, Collections.singletonList(data));
        }
    }

    @Override
    public void onWrite(Class<? extends MerlinObject> tClass, String model, String uuid, @NotNull MerlinObject data) {
        MerlinObject item = Merlin.where(tClass).equal("uuid", uuid).first();
        data.uuid = uuid;
        if (item != null) {
            // update remote data local ID with local data local ID
            Merlin.getInstance().db().write(tClass, Collections.singletonList(data));
        } else {
            Merlin.getInstance().db().create(tClass, Collections.singletonList(data));
        }
    }
}
