package utilities;

import com.microsoft.playwright.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import mcp.McpServerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import mcp.McpServerManager.McpServerFeatures;
import mcp.McpServerManager.McpSchema;

public class TestBseFrontend {
    private static final Logger log = LoggerFactory.getLogger(TestBseFrontend.class);
    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final ThreadLocal<Page> TL_PAGE = new ThreadLocal<>();
    private static final boolean MCP_ENABLED =
            !"false".equalsIgnoreCase(System.getProperty("MCP_ENABLED",
                    System.getenv().getOrDefault("MCP_ENABLED", "true")));
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    private static McpSchema.CallToolResult result(String text) {
        return new McpSchema.CallToolResult(text, false);
    }

    @BeforeSuite(alwaysRun = true)
    @Parameters("browserName")
    public void launchBrowser(@Optional("chromium") String browserName) {
        if (MCP_ENABLED) {
            McpServerManager.startIfNeeded();
            registerFrontendTools();
            log.info("[MCP] Frontend tools registered");
        }

        playwright = Playwright.create();
        log.info("Launching browser: {}", browserName);

        switch (browserName.toLowerCase()) {
            case "chromium" -> browser =
                    playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            case "firefox" -> browser =
                    playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
            case "webkit" -> browser =
                    playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
            case "edge" -> browser =
                    playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("msedge").setHeadless(false));
            default -> throw new IllegalArgumentException("Unknown browser: " + browserName);
        }
        log.info("Browser [{}] launched successfully", browserName);
    }

    @BeforeMethod(alwaysRun = true)
    public void createContextAndPage(Method method) {
        context = browser.newContext();
        log.info(">>> Start Testing: {} at {} <<<", method.getName(), LocalDateTime.now().format(fmt));
        context.tracing().start(new Tracing.StartOptions().setSnapshots(true).setScreenshots(true).setSources(true));
        page = context.newPage();
        TL_PAGE.set(page);
        log.debug("New page created in context {}", context);
    }

    @AfterMethod(alwaysRun = true)
    public void closeContext(Method method) throws IOException {
        if (context != null) {
            Files.createDirectories(Paths.get("trace-records"));
            String tn = (method.getAnnotation(Test.class) != null) ? method.getAnnotation(Test.class).testName() : null;
            String traceName = (tn != null && !tn.isEmpty()) ? tn : method.getName();

            log.info("Stopping tracing for test: {}", traceName);
            context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get("trace-records/" + traceName + ".zip")));
            log.info("Trace exported: trace-records/{}.zip", traceName);

            context.close();
            TL_PAGE.remove();
            log.debug("Context closed for test {}", traceName);
        }
        log.info(">>> Done Testing: {} at {} <<<", method.getName(), LocalDateTime.now().format(fmt));
    }

    @AfterSuite(alwaysRun = true)
    public void closeBrowser() {
        if (browser != null) {
            browser.close();
            log.info("Browser closed");
        }
        if (playwright != null) {
            playwright.close();
            log.info("Playwright closed");
        }
        if (MCP_ENABLED) {
            McpServerManager.stopIfRunning();
            log.info("[MCP] Stopped");
        }
    }

    private void registerFrontendTools() {
        var gotoSchema = """
                {"type":"object","properties":{"url":{"type":"string"}},"required":["url"]}""";
        McpServerFeatures.SyncToolSpecification webGoto =
                new McpServerFeatures.SyncToolSpecification(
                        new McpSchema.Tool("web.goto", "Navigate current Playwright page to a URL", gotoSchema),
                        null
                );
        var shotSchema = """
                {"type":"object","properties":{"path":{"type":"string","default":"screenshots/screen.png"}}}""";
        McpServerFeatures.SyncToolSpecification webScreenshot =
                new McpServerFeatures.SyncToolSpecification(
                        new McpSchema.Tool("web.screenshot", "Take a full-page screenshot", shotSchema),
                        null
                );
        McpServerFeatures.SyncToolSpecification webTitle =
                new McpServerFeatures.SyncToolSpecification(
                        new McpSchema.Tool("web.title", "Get current page title", "{type:object,properties:{}}"),
                        null
                );

        McpServerManager.registerTool(webGoto);
        McpServerManager.registerTool(webScreenshot);
        McpServerManager.registerTool(webTitle);
    }
}