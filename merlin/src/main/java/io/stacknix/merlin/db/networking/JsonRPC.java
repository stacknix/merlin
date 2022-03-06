package io.stacknix.merlin.db.networking;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonRPC {

    private final Requests requests;
    private final String endpoint;
    private final Map<String, String> headers;

    public JsonRPC(HttpScheme scheme, String host, String endpoint, Map<String, String> headers) {
        this.requests = new Requests(scheme, host);
        this.headers = headers;
        this.endpoint = endpoint;
    }

    @SuppressWarnings("unchecked")
    public <T> T call(String method, List<Object> params, long id) throws IOException, HttpException, JsonRPCException {
        Map<String, Object> body = new HashMap<>();
        body.put("jsonrpc", "2.0");
        body.put("method", method);
        body.put("params", params);
        HttpResponse response = requests.post(endpoint, headers, new HashMap<>(), HttpRequestBody.asJson(body));
        if (response.is_successful) {
            try {
                JSONObject root = new JSONObject(response.text());
                if (!root.has("error")) {
                    return (T) root.get("result");
                } else {
                    JSONObject error = root.getJSONObject("error");
                    throw new JsonRPCException(error.getInt("code"), error.getString("message"));
                }
            } catch (JSONException e) {
                throw new RuntimeException("Unexpected JSON response.");
            }
        } else {
            throw new HttpException(response.code, response.text());
        }
    }
}
