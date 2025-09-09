package utilities;

import allureReport.AllureLogger;
import drivers.MobileDriverManager;
import io.qameta.allure.testng.AllureTestNg;
import org.testng.annotations.*;

@Listeners({AllureTestNg.class})
public class TestBaseMobile<T extends TestBaseMobile<T>> {

    protected <E> E on(E pageObject) {
        return pageObject;
    }

    @BeforeMethod(alwaysRun = true)
    public void startDriver() {
        AllureLogger.step("Start Mobile Driver");
        MobileDriverManager.start();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        AllureLogger.step("Closing Mobile Driver");
        try {
            MobileDriverManager.quit();
        } catch (Exception ignored) {
        }
    }
}
