package apiTest;

import static configurations.BaseUri.*;
import static configurations.HeaderConstants.*;
import static utilities.Endpoints.*;

import dataProviders.backend.*;
import io.qameta.allure.*;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.TestBseBackend;

public class ApiTesting extends TestBseBackend {

    @Test(groups = {"Regression"})
    @Feature("API")
    @Story("Health Check")
    @Description("GET request for Health Check")
    public void healthCheck() throws Exception {
        // @INFO: without cookie
        String body = get(urlPractice(), 200, null, HEALTH_CHECK);
        Assert.assertTrue(body.contains("Notes API is Running"), "ERROR");

        // @INFO: with cookie
        String body2 = get(HEALTH_CHECK, 404, Map.of(COOKIE_TYPE, COOKIE_KEY), null);
        Assert.assertFalse(body2.contains("Notes API is Running"), "ERROR");
    }

    @Test(
            groups = {"Regression"},
            dataProvider = "registerUser",
            dataProviderClass = LoginDataProviders.class)
    @Feature("API")
    @Story("Register")
    @Description("POST request without for User Register")
    public void registerUser(Map<String, String> form,
                             int expectedStatus,
                             String expectedContains, boolean shouldContain) throws Exception {
        String body = postForm(REGISTER, form, expectedStatus, null);
        if (shouldContain) {
            Assert.assertTrue(
                    body != null && body.contains(expectedContains),
                    "Expected body to contain [" + expectedContains + "], but got: " + body);
        } else {
            Assert.assertFalse(
                    body != null && body.contains(expectedContains),
                    "Expected body NOT to contain [" + expectedContains + "], but got: " + body);
        }
    }

    @Test(
            groups = {"Sanity"},
            dataProvider = "loginUser",
            dataProviderClass = LoginDataProviders.class)
    @Feature("API")
    @Story("Login")
    @Description("POST /login with cookie – success & negative variants")
    public void loginUser(Map<String, String> form,
                          int expectedStatus,
                          String expectedContains,
                          boolean shouldContain) throws Exception {
        String body = postForm(LOGIN, form, expectedStatus, null);

        if (shouldContain) {
            Assert.assertTrue(
                    body != null && body.contains(expectedContains),
                    "Expected body to CONTAIN [" + expectedContains + "], but got: " + body);
        } else {
            Assert.assertFalse(
                    body != null && body.contains(expectedContains),
                    "Expected body to NOT CONTAIN [" + expectedContains + "], but got: " + body);
        }
    }
}
