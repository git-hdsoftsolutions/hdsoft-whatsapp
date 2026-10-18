package com.hdsoft.whatsapp.templates;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Java 8 REST client for the OpenWA session-based API.
 * <p>
 * This client intentionally exposes a generic request method so that all
 * documented REST endpoints (including endpoints added by newer OpenWA
 * releases) can be called without waiting for a new Java wrapper release.
 * <p>
 * API paths passed to request() are relative to /api, e.g.
 * /sessions or /sessions/{id}/messages/send-text.
 */
public final class OpenWAClient {
    private final String apiBaseUrl;
    private final String apiKey;
    private final int connectTimeoutMillis;
    private final int readTimeoutMillis;

    public OpenWAClient(String serverUrl, String apiKey) {
        this(serverUrl, apiKey, 10000, 40000);
    }

    public OpenWAClient(String serverUrl, String apiKey, int connectTimeoutMillis, int readTimeoutMillis) {
        if (serverUrl == null || serverUrl.trim().isEmpty())
            throw new IllegalArgumentException("serverUrl is required");
        if (apiKey == null || apiKey.trim().isEmpty()) throw new IllegalArgumentException("apiKey is required");
        if (connectTimeoutMillis <= 0 || readTimeoutMillis <= 0)
            throw new IllegalArgumentException("Timeouts must be positive");

        String normalized = serverUrl.trim().replaceAll("/+$", "");
        this.apiBaseUrl = normalized.endsWith("/api") ? normalized : normalized + "/api";
        this.apiKey = apiKey;
        this.connectTimeoutMillis = connectTimeoutMillis;
        this.readTimeoutMillis = readTimeoutMillis;
    }

    private static HttpRequestBase createRequest(String method, String url, JSONObject body) {
        if ("GET".equals(method)) return new HttpGet(url);
        if ("HEAD".equals(method)) return new HttpHead(url);
        if ("OPTIONS".equals(method)) return new HttpOptions(url);
        if ("TRACE".equals(method)) return new HttpTrace(url);
        HttpEntityEnclosingRequestBase request;
        if ("POST".equals(method)) request = new HttpPostWithBody(url);
        else if ("PUT".equals(method)) request = new HttpPutWithBody(url);
        else if ("PATCH".equals(method)) request = new HttpPatchWithBody(url);
        else if ("DELETE".equals(method)) request = new HttpDeleteWithBody(url);
        else throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        if (body != null) request.setEntity(new StringEntity(body.toString(), StandardCharsets.UTF_8));
        return request;
    }

    private static String sessionPath(String sessionId) throws IOException {
        if (sessionId == null || sessionId.trim().isEmpty())
            throw new IllegalArgumentException("sessionId is required");
        return "/sessions/" + pathSegment(sessionId);
    }

    private static String pathSegment(String value) throws IOException {
        return URLEncoder.encode(value, "UTF-8").replace("+", "%20");
    }

    private static String normalizeNumber(String number) {
        if (number == null) throw new IllegalArgumentException("Phone number is required");
        String digits = number.replaceAll("[\\s()+-]", "");
        if (!digits.matches("[0-9]{8,15}"))
            throw new IllegalArgumentException("Use international format with country code, digits only");
        return digits;
    }

    private static String requireText(String value, String name) {
        if (value == null || value.trim().isEmpty()) throw new IllegalArgumentException(name + " is required");
        return value;
    }

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    /**
     * Generic access to any REST endpoint documented by your OpenWA version.
     * Path must start with "/" and be relative to /api.
     */
    public OpenWAResponse request(String method, String path, JSONObject body) throws IOException {
        return request(method, path, body, Collections.emptyMap());
    }

    /**
     * Generic request with per-request headers, useful for Idempotency-Key.
     * Never put a per-message Idempotency-Key in global/default headers.
     */
    public OpenWAResponse request(String method, String path, JSONObject body, Map<String, String> extraHeaders) throws IOException {
        if (method == null || method.trim().isEmpty()) throw new IllegalArgumentException("HTTP method is required");
        if (path == null || !path.startsWith("/") || path.startsWith("//"))
            throw new IllegalArgumentException("Path must start with one / and be relative to /api");
        if (path.contains("://")) throw new IllegalArgumentException("Pass an API path, not a full URL");

        String url = apiBaseUrl + path;
        String verb = method.toUpperCase();
        RequestConfig config = RequestConfig.custom().setConnectTimeout(connectTimeoutMillis).setConnectionRequestTimeout(connectTimeoutMillis).setSocketTimeout(readTimeoutMillis).build();

        try (CloseableHttpClient http = HttpClients.custom().setDefaultRequestConfig(config).build()) {
            HttpRequestBase request = createRequest(verb, url, body);
            request.setHeader("X-API-Key", apiKey);
            request.setHeader("Accept", "application/json");
            if (body != null) request.setHeader("Content-Type", "application/json; charset=UTF-8");
            if (extraHeaders != null) {
                for (Map.Entry<String, String> entry : extraHeaders.entrySet()) {
                    if (entry.getKey() != null && entry.getValue() != null)
                        request.setHeader(entry.getKey(), entry.getValue());
                }
            }
            try (CloseableHttpResponse response = http.execute(request)) {
                int status = response.getStatusLine().getStatusCode();
                HttpEntity entity = response.getEntity();
                String responseBody = entity == null ? "" : EntityUtils.toString(entity, StandardCharsets.UTF_8);
                Map<String, List<String>> headers = new LinkedHashMap<String, List<String>>();
                for (Header header : response.getAllHeaders()) {
                    List<String> values = headers.get(header.getName());
                    if (values == null) {
                        values = new ArrayList<String>();
                        headers.put(header.getName(), values);
                    }
                    values.add(header.getValue());
                }
                return new OpenWAResponse(status, responseBody, headers);
            }
        }
    }

    // ---- Session lifecycle / global session management ----

    public OpenWAResponse listSessions() throws IOException {
        return request("GET", "/sessions", null);
    }

    public OpenWAResponse createSession(String name) throws IOException {
        JSONObject body = new JSONObject();
        body.put("name", name);
        return request("POST", "/sessions", body);
    }

    public OpenWAResponse getSession(String sessionId) throws IOException {
        return request("GET", sessionPath(sessionId), null);
    }

    public OpenWAResponse startSession(String sessionId) throws IOException {
        return request("POST", sessionPath(sessionId) + "/start", new JSONObject());
    }

    public OpenWAResponse stopSession(String sessionId) throws IOException {
        return request("POST", sessionPath(sessionId) + "/stop", new JSONObject());
    }

    public OpenWAResponse forceKillSession(String sessionId) throws IOException {
        return request("POST", sessionPath(sessionId) + "/force-kill", new JSONObject());
    }

    public OpenWAResponse logoutSession(String sessionId) throws IOException {
        return request("POST", sessionPath(sessionId) + "/logout", new JSONObject());
    }

    public OpenWAResponse deleteSession(String sessionId) throws IOException {
        return request("DELETE", sessionPath(sessionId), null);
    }

    public OpenWAResponse getQrCode(String sessionId) throws IOException {
        return request("GET", sessionPath(sessionId) + "/qr", null);
    }

    public OpenWAResponse requestPairingCode(String sessionId, JSONObject body) throws IOException {
        return request("POST", sessionPath(sessionId) + "/pairing-code", body == null ? new JSONObject() : body);
    }

    // ---- Common messaging and contact helpers ----

    public OpenWAResponse sendText(String sessionId, String chatId, String text) throws IOException {
        JSONObject body = new JSONObject();
        body.put("chatId", requireText(chatId, "chatId"));
        body.put("text", requireText(text, "text"));
        return request("POST", sessionPath(sessionId) + "/messages/send-text", body);
    }

    public OpenWAResponse sendText(String sessionId, String chatId, String text, String idempotencyKey) throws IOException {
        JSONObject body = new JSONObject();
        body.put("chatId", requireText(chatId, "chatId"));
        body.put("text", requireText(text, "text"));
        Map<String, String> headers = new LinkedHashMap<String, String>();
        if (idempotencyKey != null && !idempotencyKey.trim().isEmpty()) headers.put("Idempotency-Key", idempotencyKey);
        return request("POST", sessionPath(sessionId) + "/messages/send-text", body, headers);
    }

    public OpenWAResponse checkNumber(String sessionId, String internationalNumber) throws IOException {
        String number = normalizeNumber(internationalNumber);
        return request("GET", sessionPath(sessionId) + "/contacts/check/" + pathSegment(number), null);
    }

    public OpenWAResponse listContacts(String sessionId) throws IOException {
        return request("GET", sessionPath(sessionId) + "/contacts", null);
    }

    public OpenWAResponse listChats(String sessionId) throws IOException {
        return request("GET", sessionPath(sessionId) + "/chats", null);
    }

    public OpenWAResponse listGroups(String sessionId) throws IOException {
        return request("GET", sessionPath(sessionId) + "/groups", null);
    }

    public OpenWAResponse listMessages(String sessionId, String queryString) throws IOException {
        String suffix = queryString == null || queryString.trim().isEmpty() ? "" : (queryString.startsWith("?") ? queryString : "?" + queryString);
        return request("GET", sessionPath(sessionId) + "/messages" + suffix, null);
    }

    public OpenWAResponse sendImage(String sessionId, JSONObject body) throws IOException {
        return request("POST", sessionPath(sessionId) + "/messages/send-image", body);
    }

    public OpenWAResponse sendVideo(String sessionId, JSONObject body) throws IOException {
        return request("POST", sessionPath(sessionId) + "/messages/send-video", body);
    }

    public OpenWAResponse sendAudio(String sessionId, JSONObject body) throws IOException {
        return request("POST", sessionPath(sessionId) + "/messages/send-audio", body);
    }

    public OpenWAResponse sendDocument(String sessionId, JSONObject body) throws IOException {
        return request("POST", sessionPath(sessionId) + "/messages/send-document", body);
    }

    public OpenWAResponse sendLocation(String sessionId, JSONObject body) throws IOException {
        return request("POST", sessionPath(sessionId) + "/messages/send-location", body);
    }

    public OpenWAResponse sendContact(String sessionId, JSONObject body) throws IOException {
        return request("POST", sessionPath(sessionId) + "/messages/send-contact", body);
    }

    public OpenWAResponse sendPoll(String sessionId, JSONObject body) throws IOException {
        return request("POST", sessionPath(sessionId) + "/messages/send-poll", body);
    }

    public OpenWAResponse sendBulk(String sessionId, JSONObject body) throws IOException {
        return request("POST", sessionPath(sessionId) + "/messages/send-bulk", body);
    }

    public OpenWAResponse reply(String sessionId, JSONObject body) throws IOException {
        return request("POST", sessionPath(sessionId) + "/messages/reply", body);
    }

    public OpenWAResponse forward(String sessionId, JSONObject body) throws IOException {
        return request("POST", sessionPath(sessionId) + "/messages/forward", body);
    }

    public OpenWAResponse react(String sessionId, JSONObject body) throws IOException {
        return request("POST", sessionPath(sessionId) + "/messages/react", body);
    }

    public OpenWAResponse createWebhook(String sessionId, JSONObject body) throws IOException {
        return request("POST", sessionPath(sessionId) + "/webhooks", body);
    }

    public OpenWAResponse listWebhooks(String sessionId) throws IOException {
        return request("GET", sessionPath(sessionId) + "/webhooks", null);
    }

    public OpenWAResponse sendStatusText(String sessionId, JSONObject body) throws IOException {
        return request("POST", sessionPath(sessionId) + "/status/send-text", body);
    }

    public OpenWAResponse getSessionConfig(String sessionId) throws IOException {
        return request("GET", sessionPath(sessionId) + "/config", null);
    }

    public OpenWAResponse updateSessionConfig(String sessionId, JSONObject body) throws IOException {
        return request("PATCH", sessionPath(sessionId) + "/config", body);
    }

    // All other routes are available via request(method, path, body).

    private static final class HttpPostWithBody extends HttpEntityEnclosingRequestBase {
        HttpPostWithBody(String uri) {
            setURI(URI.create(uri));
        }

        @Override
        public String getMethod() {
            return "POST";
        }
    }

    private static final class HttpPutWithBody extends HttpEntityEnclosingRequestBase {
        HttpPutWithBody(String uri) {
            setURI(URI.create(uri));
        }

        @Override
        public String getMethod() {
            return "PUT";
        }
    }

    private static final class HttpPatchWithBody extends HttpEntityEnclosingRequestBase {
        HttpPatchWithBody(String uri) {
            setURI(URI.create(uri));
        }

        @Override
        public String getMethod() {
            return "PATCH";
        }
    }

    private static final class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
        HttpDeleteWithBody(String uri) {
            setURI(URI.create(uri));
        }

        @Override
        public String getMethod() {
            return "DELETE";
        }
    }

    public static final class OpenWAResponse {
        private final int statusCode;
        private final String body;
        private final Map<String, List<String>> headers;

        private OpenWAResponse(int statusCode, String body, Map<String, List<String>> headers) {
            this.statusCode = statusCode;
            this.body = body == null ? "" : body;
            this.headers = headers == null ? Collections.emptyMap() : headers;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public String getBody() {
            return body;
        }

        public Map<String, List<String>> getHeaders() {
            return headers;
        }

        public boolean isSuccessful() {
            return statusCode >= 200 && statusCode < 300;
        }

        public JSONObject asJsonObject() {
            return new JSONObject(body);
        }

        public JSONArray asJsonArray() {
            return new JSONArray(body);
        }

        public void throwIfError() throws OpenWAException {
            if (!isSuccessful()) throw new OpenWAException(statusCode, body);
        }

        @Override
        public String toString() {
            return "OpenWAResponse{statusCode=" + statusCode + ", body='" + body + "'}";
        }
    }

    public static final class OpenWAException extends IOException {
        private final int statusCode;
        private final String responseBody;

        public OpenWAException(int statusCode, String responseBody) {
            super("OpenWA returned HTTP " + statusCode + ": " + responseBody);
            this.statusCode = statusCode;
            this.responseBody = responseBody;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public String getResponseBody() {
            return responseBody;
        }
    }
}
