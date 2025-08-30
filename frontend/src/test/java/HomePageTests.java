import dataProviders.frontend.HomePageProviders;
import dataProviders.frontend.HomePageProviders.Flow;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.Description;
import org.testng.annotations.Test;
import pageObjects.HomePageObjects;
import utilities.FrontendBaseTest;

import static configurations.BaseUri.*;
import static utilities.PlaywrightUtils.*;


public class HomePageTests extends FrontendBaseTest {


    @Test(groups = {"Regression"}, dataProvider = "searchFor", dataProviderClass = HomePageProviders.class)
    @Feature("Web")
    @Story("Search")
    @Description("Open site and search with different queries")
    public void googleTitle(Flow data) {
        navigateTo(this.page, urlPractice());
        new HomePageObjects(this.page).search(data.query());
    }
}
