package io.stacknix.merlin.android.demo;

import android.content.Context;

import com.fabric.io.jsonrpc2.JsonRPCClient;
import com.fabric.io.jsonrpc2.JsonRPCResponse;
import com.fabric.io.jsonrpc2.models.ModelRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.stacknix.merlin.db.Merlin;
import io.stacknix.merlin.db.MerlinObject;
import io.stacknix.merlin.db.MerlinQuery;
import io.stacknix.merlin.db.MerlinService;
import io.stacknix.merlin.db.android.Logging;

public class JsonRPCService<T extends MerlinObject> extends MerlinService<T, Long> {

    private final ModelRequest request;

    public JsonRPCService(Class<T> tClass, JsonRPCClient client) {
        super(tClass);
        this.request = new ModelRequest(client, MerlinObject.getModelName(getObjectClass()));
    }

    @Override
    public T onCreate(Context context, T object) throws Exception {
        Map<String, Object> values = Merlin.getInstance().getMappingFactory().getData(object);
        Logging.e("P", values);
        List<String> fields = MerlinObject.getFields(getObjectClass());
        return request.create(values, fields).to(getObjectClass());
    }

    @Override
    public T onWrite(Context context, T object) throws Exception {
        final Map<String, Object> values = Merlin.getInstance().getMappingFactory().getData(object);
        request.write(getObjectId(values), values);
        return object;
    }

    @Override
    public boolean onUnlink(Context context, @NotNull T object) throws Exception {
        final Map<String, Object> values =  Merlin.getInstance().getMappingFactory().getData(object);
        return request.unlink(getObjectId(values)).to();
    }

    @Override
    public T onRead(Context context, Long id) throws Exception {
        final List<String> fields = MerlinObject.getFields(getObjectClass());
        return request.read(id, fields).to(getObjectClass());
    }

    @Override
    public List<T> onSearch(Context context, MerlinQuery<T> query) throws Exception {
        final List<String> fields = MerlinObject.getFields(getObjectClass());
        final Type type = TypeToken.getParameterized(List.class, getObjectClass()).getType();
        JsonRPCResponse response = request.search(0, fields).getResponse();
        Gson gson = new GsonBuilder().serializeNulls().create();
        String data = gson.toJson(response.result);
        return gson.fromJson(data, type);
    }

    @Override
    public void onException(Exception exception) {

    }

    private long getObjectId(@NotNull Map<String, Object> values) throws NullPointerException{
        Object objectId = values.get("id");
        if (objectId != null) {
            return (long) objectId;
        }
        throw new NullPointerException("Object has no attribute id.");
    }

}
