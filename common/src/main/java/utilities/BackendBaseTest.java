package utilities;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import static configurations.BaseUri.*;

public class BackendBaseTest {
    protected Playwright playwright;
    protected APIRequestContext request;

    @BeforeClass
    public void setupRequest() {
        playwright = Playwright.create();
        request = playwright.request().newContext(
                new APIRequest.NewContextOptions()
                        .setBaseURL(urlPractice())
        );
    }

    @AfterClass
    public void teardown() {
        if (request != null) request.dispose();
        if (playwright != null) playwright.close();
    }
}
