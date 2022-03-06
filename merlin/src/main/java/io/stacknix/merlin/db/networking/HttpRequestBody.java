package io.stacknix.merlin.db.networking;

import com.google.gson.Gson;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class HttpRequestBody {

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    final public RequestBody body;

    public HttpRequestBody(RequestBody body){
        this.body = body;
    }

    public static @NotNull HttpRequestBody asJson(@NotNull Map<String, Object> data){
        return new HttpRequestBody(RequestBody.create(new Gson().toJson(data), JSON));
    }

    public static @NotNull HttpRequestBody asForm(@NotNull Map<String, Object> data){
        FormBody.Builder formBody = new FormBody.Builder();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            formBody.add(entry.getKey(), entry.getValue().toString());
        }
        return new HttpRequestBody(formBody.build());
    }
}
