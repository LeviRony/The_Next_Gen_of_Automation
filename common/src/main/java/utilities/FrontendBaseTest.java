package utilities;

import com.microsoft.playwright.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.*;

public class FrontendBaseTest {

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;
    Logger log = LoggerFactory.getLogger(FrontendBaseTest.class);

    @BeforeSuite(alwaysRun = true)
    @Parameters("browserName")
    public void launchBrowser(@Optional("chromium") String browserName) {
        playwright = Playwright.create();
        switch (browserName.toLowerCase()) {
            case "chromium" -> browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            case "firefox"  -> browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
            case "webkit"   -> browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
            case "edge"     -> browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("msedge").setHeadless(false));
            default -> throw new IllegalArgumentException("Unknown browser: " + browserName);
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void createContextAndPage() {
        context = browser.newContext();
        log.info("Start tracing");
        context.tracing().start(new Tracing.StartOptions()
                .setSnapshots(true).setScreenshots(true).setSources(true));
        page = context.newPage();
    }

    @AfterMethod(alwaysRun = true)
    public void closeContext(Method method) throws IOException {
        if (context != null) {
            Files.createDirectories(Paths.get("trace-records"));
            String tn = method.getAnnotation(Test.class).testName();
            String traceName = (tn != null && !tn.isEmpty()) ? tn : method.getName();
            log.info("Stop tracing and save zip");
            context.tracing().stop(new Tracing.StopOptions()
                    .setPath(Paths.get("trace-records/" + traceName + ".zip")));
            System.out.println("\u001B[34m[INFO] Trace exported: trace-records/" + traceName + ".zip\u001B[0m");
            context.close();
        }
    }

    @AfterSuite(alwaysRun = true)
    public void closeBrowser() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}