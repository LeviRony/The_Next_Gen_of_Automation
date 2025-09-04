package utilities;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.FormData;
import com.microsoft.playwright.options.RequestOptions;
import configurations.BaseUri;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

// === MCP imports (same as frontend) ===
import mcp.McpServerManager;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;

public class TestBseBackend {

    private static final Logger log = LoggerFactory.getLogger(TestBseBackend.class);
    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final ThreadLocal<APIRequestContext> TL_REQ = new ThreadLocal<>();
    private static final boolean MCP_ENABLED =
            !"false".equalsIgnoreCase(System.getProperty("MCP_ENABLED",
                    System.getenv().getOrDefault("MCP_ENABLED", "true")));
    protected Playwright playwright;
    protected APIRequestContext request;

    private static String resolveUrl(String urlOrPath) {
        if (urlOrPath.startsWith("http")) return urlOrPath;
        String path = urlOrPath.startsWith("/") ? urlOrPath.substring(1) : urlOrPath;
        return BaseUri.urlPractice() + path;
    }

    private static String buildQuery(Map<String, String> q) {
        if (q == null || q.isEmpty()) return "";
        String qs = q.entrySet().stream()
                .map(e -> enCoder(e.getKey()) + "=" + enCoder(e.getValue()))
                .collect(Collectors.joining("&"));
        return "?" + qs;
    }

    private static String enCoder(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    private static String truncate(String s, int max) {
        if (s == null || s.length() <= max) return s;
        return s.substring(0, max) + " ...[truncated]";
    }

    private static String safeText(APIResponse res) {
        try {
            return res.text();
        } catch (RuntimeException e) {
            return "<non-textual body>";
        }
    }

    private static McpSchema.CallToolResult result(String text) {
        return new McpSchema.CallToolResult(text, false);
    }

    @BeforeClass(alwaysRun = true)
    public void createApiContext() {
        if (MCP_ENABLED) {
            McpServerManager.startIfNeeded();
            registerBackendTools();
            log.info("[MCP] Backend tools registered");
        }

        playwright = Playwright.create();
        request = playwright.request().newContext(new APIRequest.NewContextOptions());
        TL_REQ.set(request);
        log.info("Playwright API context created");
    }

    @AfterClass(alwaysRun = true)
    public void closeApiContext() {
        TL_REQ.remove();
        if (request != null) request.dispose();
        if (playwright != null) playwright.close();
        if (MCP_ENABLED) McpServerManager.stopIfRunning();
        log.info("Playwright API context closed");
    }

    @Step("GET {urlOrPath} with headers expects {expectedStatus}")
    public String get(String urlOrPath, int expectedStatus,
                      Map<String, String> headers, String query) {

        String url = resolveUrl(urlOrPath) + query;
        RequestOptions opts = RequestOptions.create();
        if (headers != null) headers.forEach(opts::setHeader);

        long t0 = System.nanoTime();
        APIResponse res = request.get(url, opts);
        long ms = (System.nanoTime() - t0) / 1_000_000;

        String body = res.text();
        log.info("[GET] {} -> {} ({} ms)", url, res.status(), ms);
        log.debug("Response body (first 2KB): {}", truncate(body, 2048));

        Assert.assertEquals(res.status(), expectedStatus,
                String.format("Status code is not %d. Body: %s", expectedStatus, truncate(body, 5120)));
        return body;
    }

    @Step("POST x-www-form-urlencoded to {urlOrPath} expects {expectedStatus}")
    public String postForm(String urlOrPath, Map<String, String> formFields,
                           int expectedStatus, Map<String, String> headers) {

        String url = resolveUrl(urlOrPath);
        FormData form = FormData.create();
        formFields.forEach(form::set);

        RequestOptions opts = RequestOptions.create().setForm(form);
        if (headers != null) headers.forEach(opts::setHeader);

        long t0 = System.nanoTime();
        APIResponse res = request.post(url, opts);
        long ms = (System.nanoTime() - t0) / 1_000_000;

        String body = res.text();
        log.info("[POST ] {} -> {} ({} ms)", url, res.status(), ms);
        log.debug("Response body (first 2KB): {}", truncate(body, 2048));

        Assert.assertEquals(res.status(), expectedStatus,
                String.format("Status code is not %d. Body: %s", expectedStatus, truncate(body, 5120)));
        return body;
    }

    @Step("PUT JSON to {urlOrPath} expects {expectedStatus}")
    public String putJson(String urlOrPath, Object json, int expectedStatus,
                          Map<String, String> headers, Map<String, String> query) {

        String url = resolveUrl(urlOrPath) + buildQuery(query);
        RequestOptions opts = RequestOptions.create()
                .setData(json)
                .setHeader("Content-Type", "application/json");
        if (headers != null) headers.forEach(opts::setHeader);

        long t0 = System.nanoTime();
        APIResponse res = request.put(url, opts);
        long ms = (System.nanoTime() - t0) / 1_000_000;

        String body = res.text();
        log.info("[PUT json] {} -> {} ({} ms)", url, res.status(), ms);
        log.debug("Request JSON: {}", json);
        log.debug("Response body (first 2KB): {}", truncate(body, 2048));

        Assert.assertEquals(res.status(), expectedStatus,
                String.format("Status code is not %d. Body: %s", expectedStatus, truncate(body, 5120)));
        return body;
    }

    @Step("PUT x-www-form-urlencoded to {urlOrPath} expects {expectedStatus}")
    public String putForm(String urlOrPath, Map<String, String> formFields, int expectedStatus,
                          Map<String, String> headers, Map<String, String> query) {

        String url = resolveUrl(urlOrPath) + buildQuery(query);
        FormData form = FormData.create();
        formFields.forEach(form::set);

        RequestOptions opts = RequestOptions.create().setForm(form);
        if (headers != null) headers.forEach(opts::setHeader);

        long t0 = System.nanoTime();
        APIResponse res = request.put(url, opts);
        long ms = (System.nanoTime() - t0) / 1_000_000;

        String body = res.text();
        log.info("[PUT form] {} -> {} ({} ms)", url, res.status(), ms);
        log.debug("Response body (first 2KB): {}", truncate(body, 2048));

        Assert.assertEquals(res.status(), expectedStatus,
                String.format("Status code is not %d. Body: %s", expectedStatus, truncate(body, 5120)));
        return body;
    }

    @Step("PATCH JSON to {urlOrPath} expects {expectedStatus}")
    public String patchJson(String urlOrPath, Object json, int expectedStatus,
                            Map<String, String> headers, Map<String, String> query) {

        String url = resolveUrl(urlOrPath) + buildQuery(query);
        RequestOptions opts = RequestOptions.create()
                .setData(json)
                .setHeader("Content-Type", "application/json");
        if (headers != null) headers.forEach(opts::setHeader);

        long t0 = System.nanoTime();
        APIResponse res = request.patch(url, opts);
        long ms = (System.nanoTime() - t0) / 1_000_000;

        String body = res.text();
        log.info("[PATCH json] {} -> {} ({} ms)", url, res.status(), ms);
        log.debug("Request JSON: {}", json);
        log.debug("Response body (first 2KB): {}", truncate(body, 2048));

        Assert.assertEquals(res.status(), expectedStatus,
                String.format("Status code is not %d. Body: %s", expectedStatus, truncate(body, 5120)));
        return body;
    }

    @Step("PATCH x-www-form-urlencoded to {urlOrPath} expects {expectedStatus}")
    public String patchForm(String urlOrPath, Map<String, String> formFields, int expectedStatus,
                            Map<String, String> headers, Map<String, String> query) {

        String url = resolveUrl(urlOrPath) + buildQuery(query);
        FormData form = FormData.create();
        formFields.forEach(form::set);

        RequestOptions opts = RequestOptions.create().setForm(form);
        if (headers != null) headers.forEach(opts::setHeader);

        long t0 = System.nanoTime();
        APIResponse res = request.patch(url, opts);
        long ms = (System.nanoTime() - t0) / 1_000_000;

        String body = res.text();
        log.info("[PATCH form] {} -> {} ({} ms)", url, res.status(), ms);
        log.debug("Response body (first 2KB): {}", truncate(body, 2048));

        Assert.assertEquals(res.status(), expectedStatus,
                String.format("Status code is not %d. Body: %s", expectedStatus, truncate(body, 5120)));
        return body;
    }

    @Step("DELETE {urlOrPath} expects {expectedStatus}")
    public String delete(String urlOrPath, int expectedStatus,
                         Map<String, String> headers, Map<String, String> query) {

        String url = resolveUrl(urlOrPath) + buildQuery(query);
        RequestOptions opts = RequestOptions.create();
        if (headers != null) headers.forEach(opts::setHeader);

        long t0 = System.nanoTime();
        APIResponse res = request.delete(url, opts);
        long ms = (System.nanoTime() - t0) / 1_000_000;

        String body = res.text();
        log.info("[DELETE] {} -> {} ({} ms)", url, res.status(), ms);
        log.debug("Response body (first 2KB): {}", truncate(body, 2048));

        Assert.assertEquals(res.status(), expectedStatus,
                String.format("Status code is not %d. Body: %s", expectedStatus, truncate(body, 5120)));
        return body;
    }

    @Step("DELETE JSON to {urlOrPath} expects {expectedStatus}")
    public String deleteJson(String urlOrPath, Object json, int expectedStatus,
                             Map<String, String> headers, Map<String, String> query) {

        String url = resolveUrl(urlOrPath) + buildQuery(query);
        RequestOptions opts = RequestOptions.create()
                .setData(json)
                .setHeader("Content-Type", "application/json");
        if (headers != null) headers.forEach(opts::setHeader);

        long t0 = System.nanoTime();
        APIResponse res = request.delete(url, opts);
        long ms = (System.nanoTime() - t0) / 1_000_000;

        String body = res.text();
        log.info("[DELETE json] {} -> {} ({} ms)", url, res.status(), ms);
        log.debug("Request JSON: {}", json);
        log.debug("Response body (first 2KB): {}", truncate(body, 2048));

        Assert.assertEquals(res.status(), expectedStatus,
                String.format("Status code is not %d. Body: %s", expectedStatus, truncate(body, 5120)));
        return body;
    }

    @Step("{method} {urlOrPath} expects {expectedStatus}")
    public String http(String method, String urlOrPath, int expectedStatus,
                       Map<String, String> headers, String query) {
        HttpMethod m = HttpMethod.valueOf(method.toUpperCase());
        switch (m) {
            case GET:
                return get(urlOrPath, expectedStatus, headers, query);
            default:
                throw new IllegalArgumentException(
                        "http(...) here only demonstrates GET. Use postJson/postForm/... for others.");
        }
    }

    private void registerBackendTools() {
        final String headersQueryProps = """
                  "headers":{"type":"object","additionalProperties":{"type":"string"}},
                  "query":{"type":"object","additionalProperties":{"type":"string"}},
                  "expectedStatus":{"type":"integer","minimum":100,"maximum":599}
                """;

        var getSchema = """
                {"type":"object","properties":{
                  "urlOrPath":{"type":"string"},
                """ + headersQueryProps + """
                },"required":["urlOrPath"]}""";
        McpServerFeatures.SyncToolSpecification apiGet =
                new McpServerFeatures.SyncToolSpecification(
                        new McpSchema.Tool("api.get", "HTTP GET via Playwright APIRequestContext", getSchema),
                        (exchange, args) -> {
                            APIRequestContext r = TL_REQ.get();
                            if (r == null) return result("ERROR: no active APIRequestContext");
                            String urlOrPath = (String) args.get("urlOrPath");
                            @SuppressWarnings("unchecked")
                            Map<String, String> headers = (Map<String, String>) args.get("headers");
                            @SuppressWarnings("unchecked")
                            Map<String, String> query = (Map<String, String>) args.get("query");
                            Integer expected = (Integer) args.get("expectedStatus");

                            String url = resolveUrl(urlOrPath) + buildQuery(query);
                            RequestOptions opts = RequestOptions.create();
                            if (headers != null) headers.forEach(opts::setHeader);

                            APIResponse res = r.get(url, opts);
                            String body = safeText(res);
                            if (expected != null && res.status() != expected) {
                                return result("FAIL " + res.status() + " body: " + truncate(body, 1024));
                            }
                            return result("OK " + res.status() + " body: " + truncate(body, 1024));
                        }
                );
        var postFormSchema = """
                {"type":"object","properties":{
                  "urlOrPath":{"type":"string"},
                  "form":{"type":"object","additionalProperties":{"type":"string"}},
                """ + headersQueryProps + """
                },"required":["urlOrPath","form"]}""";
        McpServerFeatures.SyncToolSpecification apiPostForm =
                new McpServerFeatures.SyncToolSpecification(
                        new McpSchema.Tool("api.postForm", "POST x-www-form-urlencoded", postFormSchema),
                        (exchange, args) -> {
                            APIRequestContext r = TL_REQ.get();
                            if (r == null) return result("ERROR: no active APIRequestContext");
                            String urlOrPath = (String) args.get("urlOrPath");
                            @SuppressWarnings("unchecked")
                            Map<String, String> form = (Map<String, String>) args.get("form");
                            @SuppressWarnings("unchecked")
                            Map<String, String> headers = (Map<String, String>) args.get("headers");
                            Integer expected = (Integer) args.get("expectedStatus");

                            String url = resolveUrl(urlOrPath);
                            FormData fd = FormData.create();
                            if (form != null) form.forEach(fd::set);

                            RequestOptions opts = RequestOptions.create().setForm(fd);
                            if (headers != null) headers.forEach(opts::setHeader);

                            APIResponse res = r.post(url, opts);
                            String body = safeText(res);
                            if (expected != null && res.status() != expected) {
                                return result("FAIL " + res.status() + " body: " + truncate(body, 1024));
                            }
                            return result("OK " + res.status() + " body: " + truncate(body, 1024));
                        }
                );

        var jsonSchema = """
                {"type":"object","properties":{
                  "urlOrPath":{"type":"string"},
                  "json":{"type":"object"},
                """ + headersQueryProps + """
                },"required":["urlOrPath","json"]}""";

        McpServerFeatures.SyncToolSpecification apiPutJson =
                new McpServerFeatures.SyncToolSpecification(
                        new McpSchema.Tool("api.putJson", "PUT JSON", jsonSchema),
                        (exchange, args) -> jsonMethod("PUT", args)
                );

        McpServerFeatures.SyncToolSpecification apiPatchJson =
                new McpServerFeatures.SyncToolSpecification(
                        new McpSchema.Tool("api.patchJson", "PATCH JSON", jsonSchema),
                        (exchange, args) -> jsonMethod("PATCH", args)
                );

        McpServerFeatures.SyncToolSpecification apiDeleteJson =
                new McpServerFeatures.SyncToolSpecification(
                        new McpSchema.Tool("api.deleteJson", "DELETE JSON", jsonSchema),
                        (exchange, args) -> jsonMethod("DELETE_JSON", args) // special flag to use body
                );

        var delSchema = """
                {"type":"object","properties":{
                  "urlOrPath":{"type":"string"},
                """ + headersQueryProps + """
                },"required":["urlOrPath"]}""";
        McpServerFeatures.SyncToolSpecification apiDelete =
                new McpServerFeatures.SyncToolSpecification(
                        new McpSchema.Tool("api.delete", "DELETE (no body)", delSchema),
                        (exchange, args) -> {
                            APIRequestContext r = TL_REQ.get();
                            if (r == null) return result("ERROR: no active APIRequestContext");
                            String urlOrPath = (String) args.get("urlOrPath");
                            @SuppressWarnings("unchecked")
                            Map<String, String> headers = (Map<String, String>) args.get("headers");
                            Integer expected = (Integer) args.get("expectedStatus");
                            @SuppressWarnings("unchecked")
                            Map<String, String> query = (Map<String, String>) args.get("query");

                            String url = resolveUrl(urlOrPath) + buildQuery(query);
                            RequestOptions opts = RequestOptions.create();
                            if (headers != null) headers.forEach(opts::setHeader);

                            APIResponse res = r.delete(url, opts);
                            String body = safeText(res);
                            if (expected != null && res.status() != expected) {
                                return result("FAIL " + res.status() + " body: " + truncate(body, 1024));
                            }
                            return result("OK " + res.status() + " body: " + truncate(body, 1024));
                        }
                );

        McpServerManager.registerTool(apiGet);
        McpServerManager.registerTool(apiPostForm);
        McpServerManager.registerTool(apiPutJson);
        McpServerManager.registerTool(apiPatchJson);
        McpServerManager.registerTool(apiDeleteJson);
        McpServerManager.registerTool(apiDelete);
    }

    private McpSchema.CallToolResult jsonMethod(String verb, Map<String, Object> args) {
        APIRequestContext r = TL_REQ.get();
        if (r == null) return result("ERROR: no active APIRequestContext");

        String urlOrPath = (String) args.get("urlOrPath");
        Object json = args.get("json");
        @SuppressWarnings("unchecked")
        Map<String, String> headers = (Map<String, String>) args.get("headers");
        Integer expected = (Integer) args.get("expectedStatus");
        @SuppressWarnings("unchecked")
        Map<String, String> query = (Map<String, String>) args.get("query");

        String url = resolveUrl(urlOrPath) + buildQuery(query);
        RequestOptions opts = RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setData(json);
        if (headers != null) headers.forEach(opts::setHeader);

        APIResponse res;
        switch (verb) {
            case "PUT" -> res = r.put(url, opts);
            case "PATCH" -> res = r.patch(url, opts);
            case "DELETE_JSON" -> res = r.delete(url, opts);
            default -> {
                return result("ERROR: unsupported verb " + verb);
            }
        }

        String body = safeText(res);
        if (expected != null && res.status() != expected) {
            return result("FAIL " + res.status() + " body: " + truncate(body, 1024));
        }
        return result("OK " + res.status() + " body: " + truncate(body, 1024));
    }

    public enum HttpMethod {GET, POST, PUT, PATCH, DELETE}
}
