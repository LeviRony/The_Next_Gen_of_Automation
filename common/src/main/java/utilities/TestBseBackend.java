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


public class TestBseBackend {

    private static final Logger log = LoggerFactory.getLogger(TestBseBackend.class);
    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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

    @BeforeClass(alwaysRun = true)
    public void createApiContext() {
        playwright = Playwright.create();
        request = playwright.request().newContext(new APIRequest.NewContextOptions());
        log.info("Playwright API context created");
    }

    @AfterClass(alwaysRun = true)
    public void closeApiContext() {
        if (request != null) request.dispose();
        if (playwright != null) playwright.close();
        log.info("Playwright API context closed");
    }

//    @BeforeMethod(alwaysRun = true)
//    public void startTesting(Method method) {
//        log.info(">>> Start Testing: {} at {} <<<",
//                method.getName(),
//                LocalDateTime.now().format(fmt));
//    }
//
//    @AfterMethod(alwaysRun = true)
//    public void testEnded(Method method) {
//        log.info(">>> Done Testing: {} at {} <<<",
//                method.getName(),
//                LocalDateTime.now().format(fmt));
//    }

    // ---------- GET ----------
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

    // ---------- POST x-www-form-urlencoded ----------
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

    // ---------- PUT JSON ----------
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

    // ---------- PUT form ----------
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

    // ---------- PATCH JSON ----------
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

    // ---------- PATCH form ----------
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

    // ---------- DELETE (no body) ----------
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

    // ---------- DELETE with JSON body ----------
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

    public enum HttpMethod {GET, POST, PUT, PATCH, DELETE}
}
