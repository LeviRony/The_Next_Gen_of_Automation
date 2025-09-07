import dataProviders.mobile.LoginApp.HomePage;
import dataProviders.mobile.LoginApp.LoginPage;
import io.appium.java_client.AppiumDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.TestBaseMobile;

import static drivers.MobileDriverManager.*;

public class LoginTest extends TestBaseMobile {


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
