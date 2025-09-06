import allureReport.AllureLogger;
import dataProviders.mobile.LoginApp.HomePage;
import dataProviders.mobile.LoginApp.LoginPage;
import io.appium.java_client.AppiumDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.TestBseMobile;

import static drivers.MobileDriverManager.*;

public class LoginTest extends TestBseMobile {


    @Test(groups = {"Regression"})
    public void canLogin() {
        AppiumDriver d = get();
        HomePage home = new LoginPage(d)
                .typeUsername("Rony")
                .typePassword("myTestPass")
                .submit();
        Assert.assertTrue(home.bannerText().contains("Welcome"));
    }
}
