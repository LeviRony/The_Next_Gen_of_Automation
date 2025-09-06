package dataProviders.mobile.LoginApp;

import allureReport.AllureLogger;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;

public class LoginPage {
    private final AppiumDriver driver;

    public LoginPage(AppiumDriver driver) {
        this.driver = driver;
    }

    public LoginPage typeUsername(String u) {
        AllureLogger.step("Entering userName");
        driver.findElement(AppiumBy.accessibilityId("username")).sendKeys(u);
        return this;
    }

    public LoginPage typePassword(String p) {
        AllureLogger.step("Entering user password");
        driver.findElement(AppiumBy.accessibilityId("password")).sendKeys(p);
        return this;
    }

    public HomePage submit() {
        AllureLogger.step("Click login button");
        driver.findElement(AppiumBy.accessibilityId("loginButton")).click();
        return new HomePage(driver);
    }
}
