package io.stacknix.merlin.db.networking;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Response;

public class HttpResponse {

    private final Response response;
    public int code;
    public boolean is_successful;

    public HttpResponse(@NotNull Response response) throws IOException {
        this.response = response;
        this.code = response.code();
        this.is_successful = response.isSuccessful();
    }

    public String text() throws IOException {
        return Objects.requireNonNull(response.body()).string();
    }
}
