package dataProviders.mobile.LoginApp;

import allureReport.AllureLogger;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;

public class HomePage {
    private final AppiumDriver driver;

    public HomePage(AppiumDriver driver) {
        this.driver = driver;
    }

    public String bannerText() {
        AllureLogger.step("Validation test: homeBanner");
        WebElement el = driver.findElement(AppiumBy.accessibilityId("homeBanner"));
        return el.getText();
    }
}
