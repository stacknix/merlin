package io.stacknix.merlin.android.demo;

import android.content.Context;

import com.fabric.io.jsonrpc2.JsonRPCClient;
import com.fabric.io.jsonrpc2.models.ModelRequest;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.stacknix.merlin.db.Merlin;
import io.stacknix.merlin.db.MerlinObject;
import io.stacknix.merlin.db.MerlinQuery;
import io.stacknix.merlin.db.MerlinService;

public class JsonRPCService<T extends MerlinObject> extends MerlinService<T, Long> {

    private final ModelRequest request;

    public JsonRPCService(Class<T> tClass, JsonRPCClient client) {
        super(tClass);
        this.request = new ModelRequest(client, MerlinObject.getModelName(getObjectClass()));
    }

    @Override
    public T onCreate(Context context, T object) throws Exception {
        Map<String, Object> values = Merlin.getInstance().getMappingFactory().getValues(object);
        List<String> fields = MerlinObject.getFields(getObjectClass());
        return request.create(values, fields).to(getObjectClass());
    }

    @Override
    public T onWrite(Context context, T object) throws Exception {
        final Map<String, Object> values = Merlin.getInstance().getMappingFactory().getValues(object);
        request.write(getObjectId(values), values);
        return object;
    }

    @Override
    public boolean onUnlink(Context context, @NotNull T object) throws Exception {
        final Map<String, Object> values =  Merlin.getInstance().getMappingFactory().getValues(object);
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
        return request.search(0, fields).to(type);
    }

    private long getObjectId(@NotNull Map<String, Object> values) throws NullPointerException{
        Object objectId = values.get("id");
        if (objectId != null) {
            return (long) objectId;
        }
        throw new NullPointerException("Object has no attribute id.");
    }

}
