package io.stacknix.merlin.db.networking;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Requests {

    private final OkHttpClient.Builder builder;
    private final String host;
    private final HttpScheme scheme;

    public Requests(HttpScheme scheme, String host) {
        this.scheme = scheme;
        this.host = host;
        this.builder = new OkHttpClient.Builder();
    }

    private @NotNull Request.Builder createRequest(@NotNull String endpoint, @NotNull Map<String, String> headers, @NotNull Map<String, String> params) {
        HttpUrl.Builder httpUrl = new HttpUrl.Builder()
                .scheme(scheme.name().toLowerCase())
                .host(this.host)
                .addPathSegment(endpoint);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            httpUrl.addQueryParameter(entry.getKey(), entry.getValue());
        }

        Request.Builder request = new Request.Builder()
                .url(httpUrl.build());
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            request.header(entry.getKey(), entry.getValue());
        }

        return request;
    }

    public @NotNull HttpResponse get(@NotNull String endpoint, @NotNull Map<String, String> headers, @NotNull Map<String, String> params) throws IOException {
        Request request = createRequest(endpoint, headers, params).get().build();
        return new HttpResponse(builder.build().newCall(request).execute());
    }

    public @NotNull HttpResponse post(@NotNull String endpoint, @NotNull Map<String, String> headers, @NotNull Map<String, String> params, @NotNull HttpRequestBody body) throws IOException {
        Request request = createRequest(endpoint, headers, params).post(body.body).build();
        return new HttpResponse(builder.build().newCall(request).execute());
    }

    public @NotNull HttpResponse put(@NotNull String endpoint, @NotNull Map<String, String> headers, @NotNull Map<String, String> params, @NotNull HttpRequestBody body) throws IOException {
        Request request = createRequest(endpoint, headers, params).put(body.body).build();
        return new HttpResponse(builder.build().newCall(request).execute());
    }

    public @NotNull HttpResponse patch(@NotNull String endpoint, @NotNull Map<String, String> headers, @NotNull Map<String, String> params, @NotNull HttpRequestBody body) throws IOException {
        Request request = createRequest(endpoint, headers, params).patch(body.body).build();
        return new HttpResponse(builder.build().newCall(request).execute());
    }

    public @NotNull HttpResponse delete(@NotNull String endpoint, @NotNull Map<String, String> headers, @NotNull Map<String, String> params, @NotNull HttpRequestBody body) throws IOException {
        Request request = createRequest(endpoint, headers, params).delete(body.body).build();
        return new HttpResponse(builder.build().newCall(request).execute());
    }
}
