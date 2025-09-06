package dataProviders.frontend;

import allureReport.AllureLogger;
import org.testng.annotations.DataProvider;

public class HomePageProviders {
    @DataProvider(name = "searchFor")
    public static Object[][] searchFor() {
        AllureLogger.step("Entering searching data");
        return new Object[][]{{new Flow("QA Testing", true)}, {new Flow("Playwright Java", false)}};
    }

    public record Flow(String query, boolean clickAd) {
    }
}
