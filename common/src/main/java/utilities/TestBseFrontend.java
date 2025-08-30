package utilities;

import com.microsoft.playwright.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;


public class TestBseFrontend {
    private static final Logger log = LoggerFactory.getLogger(TestBseFrontend.class);
    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeSuite(alwaysRun = true)
    @Parameters("browserName")
    public void launchBrowser(@Optional("chromium") String browserName) {
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
                    playwright.chromium()
                            .launch(new BrowserType.LaunchOptions().setChannel("msedge").setHeadless(false));
            default -> throw new IllegalArgumentException("Unknown browser: " + browserName);
        }

        log.info("Browser [{}] launched successfully", browserName);
    }

    @BeforeMethod(alwaysRun = true)
    public void createContextAndPage(Method method) {
        context = browser.newContext();
        log.info(">>> Start Testing: {} at {} <<<",
                method.getName(),
                LocalDateTime.now().format(fmt));
        context.tracing()
                .start(new Tracing.StartOptions()
                        .setSnapshots(true)
                        .setScreenshots(true)
                        .setSources(true));
        page = context.newPage();
        log.debug("New page created in context {}", context);
    }

    @AfterMethod(alwaysRun = true)
    public void closeContext(Method method) throws IOException {
        if (context != null) {
            Files.createDirectories(Paths.get("trace-records"));

            String tn = (method.getAnnotation(Test.class) != null)
                    ? method.getAnnotation(Test.class).testName()
                    : null;
            String traceName = (tn != null && !tn.isEmpty()) ? tn : method.getName();

            log.info("Stopping tracing for test: {}", traceName);

            context.tracing().stop(
                    new Tracing.StopOptions().setPath(Paths.get("trace-records/" + traceName + ".zip")));

            log.info("Trace exported: trace-records/{}.zip", traceName);
            context.close();
            log.debug("Context closed for test {}", traceName);
        }
        log.info(">>> Done Testing: {} at {} <<<",
                method.getName(),
                LocalDateTime.now().format(fmt));
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
    }
}
