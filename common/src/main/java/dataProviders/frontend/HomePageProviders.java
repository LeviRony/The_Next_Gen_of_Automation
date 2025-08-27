package dataProviders.frontend;

import org.testng.annotations.DataProvider;
import utilities.FrontendBaseTest;

public class HomePageProviders {
    public record Flow(String query, boolean clickAd) {}

    @DataProvider(name = "searchFor")
    public static Object[][] searchFor() {
        return new Object[][]{
                { new Flow("QA Testing", true) },
                { new Flow("Playwright Java", false) }
        };
    }
}