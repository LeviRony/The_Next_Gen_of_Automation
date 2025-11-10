import static configurations.BaseUri.*;
import static utilities.Ansi.*;
import static utilities.PlaywrightUtils.*;

import dataProviders.frontend.HomePageProviders;
import dataProviders.frontend.HomePageProviders.Flow;
import io.qameta.allure.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObjects.HomePageObjects;
import utilities.TestBseFrontend;

import java.lang.reflect.Method;


@Epic("Web")
@Feature("Website ")
@Story("Main")
public class HomePageTests extends TestBseFrontend {

    @BeforeMethod(alwaysRun = true)
    public void preTestPreparation(Method method)  {
        switch (method.getName()) {
            case "googleTitle" -> {

                log.info(ORANGE + "[PREP] Prep for {}", method.getName() + RESET);
            }
            case "" -> {
                //Action: send a post req of user data for testing
                log.info(ORANGE + "[PREP] Prep for {}", method.getName() + RESET);
            }
            default -> log.info("[PREP] No special prep needed for {}", method.getName());
        }
    }

    @Test(
            groups = {"Regression"},
            dataProvider = "searchFor",
            dataProviderClass = HomePageProviders.class)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Open site and search with different queries")
    public void googleTitle(Flow data) {
        navigateTo(this.page, urlPractice());
        new HomePageObjects(this.page).search(data.query());
    }
}
