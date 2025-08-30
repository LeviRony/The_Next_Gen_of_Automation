package pageObjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;

public class HomePageObjects {

  private final Page page;

  public HomePageObjects(Page page) {
    this.page = page;
  }

  private Locator searchBox() {
    return page.getByRole(
        AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search an example..."));
  }

  private Locator searchBtn() {
    return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search"));
  }

  private Locator PlaywrightResult() {
    return page.getByRole(AriaRole.LISTITEM)
        .filter(new Locator.FilterOptions().setHasText("Playwright"));
  }

  @Step("Search for: {query}")
  public void search(String query) {
    searchBox().click();
    searchBox().fill(query);
    PlaywrightResult().isVisible();
  }
}
