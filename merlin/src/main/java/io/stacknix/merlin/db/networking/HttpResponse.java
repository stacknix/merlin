package io.stacknix.merlin.db.networking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.Response;

public class HttpResponse {

    private final Response response;
    public int code;

    public HttpResponse(@NotNull Response response) throws IOException {
        this.response = response;
        this.code = response.code();
    }

    public Map<String, Object> json() throws IOException {
        return new Gson().fromJson(
                text(), new TypeToken<HashMap<String, Object>>() {}.getType()
        );
    }

    public String text() throws IOException {
        return Objects.requireNonNull(response.body()).string();
    }
}
