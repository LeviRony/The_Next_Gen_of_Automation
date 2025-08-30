package utilities;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.FormData;
import com.microsoft.playwright.options.RequestOptions;
import configurations.BaseUri;
import io.qameta.allure.Step;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;
import org.testng.Assert;
import org.testng.annotations.*;

public class BackendBaseTest {
  protected Playwright playwright;
  protected APIRequestContext request;

  private static String resolveUrl(String urlOrPath) {
    if (urlOrPath.startsWith("http")) return urlOrPath;
    String path = urlOrPath.startsWith("/") ? urlOrPath.substring(1) : urlOrPath;
    return BaseUri.urlPractice() + path; // e.g., "https://practice.expandtesting.com/"
  }

  private static String buildQuery(Map<String, String> q) {
    if (q == null || q.isEmpty()) return "";
    String qs =
        q.entrySet().stream()
            .map(e -> enc(e.getKey()) + "=" + enc(e.getValue()))
            .collect(Collectors.joining("&"));
    return "?" + qs;
  }

  private static String enc(String s) {
    return URLEncoder.encode(s, StandardCharsets.UTF_8);
  }

  @BeforeClass(alwaysRun = true)
  public void createApiContext() {
    playwright = Playwright.create();
    request = playwright.request().newContext(new APIRequest.NewContextOptions());
  }

  @AfterClass(alwaysRun = true)
  public void closeApiContext() {
    if (request != null) request.dispose();
    if (playwright != null) playwright.close();
  }

  //    @BeforeMethod(alwaysRun = true)
  //    public void logCase(Method m, Object[] params) {
  //        System.out.println("RUNNING: " + m.getName() + " " + Arrays.toString(params));
  //    }

  // ---------- GET ----------
  @Step("GET {urlOrPath} with headers expects {expectedStatus}")
  public String get(
      String urlOrPath,
      int expectedStatus,
      Map<String, String> headers,
      Map<String, String> query) {
    String url = resolveUrl(urlOrPath) + buildQuery(query);
    RequestOptions opts = RequestOptions.create();
    if (headers != null) headers.forEach(opts::setHeader);
    APIResponse res = request.get(url, opts);
    String body = res.text();
    System.out.println("[GET] " + url);
    System.out.println("Status: " + res.status());
    System.out.println(body);
    Assert.assertEquals(
        res.status(), expectedStatus, "Status code is not " + expectedStatus + ". Body: " + body);
    return body;
  }

  // ---------- POST JSON ----------
  @Step("POST x-www-form-urlencoded to {urlOrPath} expects {expectedStatus}")
  public String postForm(
      String urlOrPath,
      Map<String, String> formFields,
      int expectedStatus,
      Map<String, String> headers) {

    String url = resolveUrl(urlOrPath);
    FormData form = FormData.create();
    formFields.forEach(form::set);
    RequestOptions opts = RequestOptions.create().setForm(form);
    if (headers != null) headers.forEach(opts::setHeader);
    APIResponse res = request.post(url, opts);
    String body = res.text();
    System.out.println("[POST] " + url);
    System.out.println("Status: " + res.status());
    System.out.println(body);
    Assert.assertEquals(
        res.status(), expectedStatus, "Status code is not " + expectedStatus + ". Body: " + body);

    return body;
  }

  // ---------- PUT JSON ----------
  @Step("PUT JSON to {urlOrPath} expects {expectedStatus}")
  public String putJson(
      String urlOrPath,
      Object json, // Map/POJO/String
      int expectedStatus,
      Map<String, String> headers,
      Map<String, String> query) {
    String url = resolveUrl(urlOrPath) + buildQuery(query);
    RequestOptions opts =
        RequestOptions.create().setData(json).setHeader("Content-Type", "application/json");
    if (headers != null) headers.forEach(opts::setHeader);

    APIResponse res = request.put(url, opts);
    String body = res.text();

    System.out.println("[PUT] " + url);
    System.out.println("Status: " + res.status());
    System.out.println(body);

    Assert.assertEquals(
        res.status(), expectedStatus, "Status code is not " + expectedStatus + ". Body: " + body);
    return body;
  }

  // ---------- PUT ----------
  @Step("PUT x-www-form-urlencoded to {urlOrPath} expects {expectedStatus}")
  public String putForm(
      String urlOrPath,
      Map<String, String> formFields,
      int expectedStatus,
      Map<String, String> headers,
      Map<String, String> query) {
    String url = resolveUrl(urlOrPath) + buildQuery(query);
    FormData form = FormData.create();
    formFields.forEach(form::set);

    RequestOptions opts = RequestOptions.create().setForm(form); // sets Content-Type automatically
    if (headers != null) headers.forEach(opts::setHeader);

    APIResponse res = request.put(url, opts);
    String body = res.text();

    System.out.println("[PUT] " + url);
    System.out.println("Status: " + res.status());
    System.out.println(body);

    Assert.assertEquals(
        res.status(), expectedStatus, "Status code is not " + expectedStatus + ". Body: " + body);
    return body;
  }

  // ---------- PATCH JSON ----------
  @Step("PATCH JSON to {urlOrPath} expects {expectedStatus}")
  public String patchJson(
      String urlOrPath,
      Object json,
      int expectedStatus,
      Map<String, String> headers,
      Map<String, String> query) {
    String url = resolveUrl(urlOrPath) + buildQuery(query);
    RequestOptions opts =
        RequestOptions.create().setData(json).setHeader("Content-Type", "application/json");
    if (headers != null) headers.forEach(opts::setHeader);

    APIResponse res = request.patch(url, opts);
    String body = res.text();

    System.out.println("[PATCH] " + url);
    System.out.println("Status: " + res.status());
    System.out.println(body);

    Assert.assertEquals(
        res.status(), expectedStatus, "Status code is not " + expectedStatus + ". Body: " + body);
    return body;
  }

  // ---------- PATCH ----------
  @Step("PATCH x-www-form-urlencoded to {urlOrPath} expects {expectedStatus}")
  public String patchForm(
      String urlOrPath,
      Map<String, String> formFields,
      int expectedStatus,
      Map<String, String> headers,
      Map<String, String> query) {
    String url = resolveUrl(urlOrPath) + buildQuery(query);
    FormData form = FormData.create();
    formFields.forEach(form::set);

    RequestOptions opts = RequestOptions.create().setForm(form);
    if (headers != null) headers.forEach(opts::setHeader);

    APIResponse res = request.patch(url, opts);
    String body = res.text();

    System.out.println("[PATCH] " + url);
    System.out.println("Status: " + res.status());
    System.out.println(body);

    Assert.assertEquals(
        res.status(), expectedStatus, "Status code is not " + expectedStatus + ". Body: " + body);
    return body;
  }

  // ---------- DELETE (no body) ----------
  @Step("DELETE {urlOrPath} expects {expectedStatus}")
  public String delete(
      String urlOrPath,
      int expectedStatus,
      Map<String, String> headers,
      Map<String, String> query) {
    String url = resolveUrl(urlOrPath) + buildQuery(query);
    RequestOptions opts = RequestOptions.create();
    if (headers != null) headers.forEach(opts::setHeader);

    APIResponse res = request.delete(url, opts);
    String body = res.text();

    System.out.println("[DELETE] " + url);
    System.out.println("Status: " + res.status());
    System.out.println(body);

    Assert.assertEquals(
        res.status(), expectedStatus, "Status code is not " + expectedStatus + ". Body: " + body);
    return body;
  }

  // ---------- DELETE with JSON body ----------
  @Step("DELETE JSON to {urlOrPath} expects {expectedStatus}")
  public String deleteJson(
      String urlOrPath,
      Object json,
      int expectedStatus,
      Map<String, String> headers,
      Map<String, String> query) {
    String url = resolveUrl(urlOrPath) + buildQuery(query);
    RequestOptions opts =
        RequestOptions.create().setData(json).setHeader("Content-Type", "application/json");
    if (headers != null) headers.forEach(opts::setHeader);

    APIResponse res = request.delete(url, opts);
    String body = res.text();

    System.out.println("[DELETE] " + url);
    System.out.println("Status: " + res.status());
    System.out.println(body);

    Assert.assertEquals(
        res.status(), expectedStatus, "Status code is not " + expectedStatus + ". Body: " + body);
    return body;
  }

  @Step("{method} {urlOrPath} expects {expectedStatus}")
  public String http(
      String method,
      String urlOrPath,
      int expectedStatus,
      Map<String, String> headers,
      Map<String, String> query) {
    HttpMethod m = HttpMethod.valueOf(method.toUpperCase());
    switch (m) {
      case GET:
        return get(urlOrPath, expectedStatus, headers, query);
      default:
        throw new IllegalArgumentException(
            "http(...) here only demonstrates GET. "
                + "Use your existing postJson/postForm helpers for other methods.");
    }
  }

  public enum HttpMethod {
    GET,
    POST,
    PUT,
    PATCH,
    DELETE
  }
}
