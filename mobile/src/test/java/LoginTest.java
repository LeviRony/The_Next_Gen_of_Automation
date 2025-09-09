import dataProviders.mobile.LoginApp.HomePage;
import dataProviders.mobile.LoginApp.LoginPage;
import drivers.MobileDriverManager;
import io.appium.java_client.AppiumDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.TestBaseMobile;

public class LoginTest extends TestBaseMobile {


    @Test(groups = {"Regression"})
    public void canLogin() {
        AppiumDriver d = MobileDriverManager.get();
        HomePage home = new LoginPage(d)
                .typeUsername("Rony")
                .typePassword("myTestPass")
                .submit();
        Assert.assertTrue(home.bannerText().contains("Welcome"));
    }
}
