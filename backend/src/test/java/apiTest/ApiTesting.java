package apiTest;


import dataProviders.backend.*;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.BackendBaseTest;

import java.util.Map;

import static utilities.Endpoints.*;


public class ApiTesting extends BackendBaseTest {

    @Test(groups = {"Regression"})
    @Feature("API")
    @Story("Health Check")
    @Description("GET request for Health Check")
    public void healthCheck() throws Exception {
        String body = get(HEALTH_CHECK, 200, null, null);
        Assert.assertTrue(body.contains("Notes API is Running"), "ERROR");
    }

    @Test(groups = {"Regression"}, dataProvider = "registerUser", dataProviderClass = LoginDataProviders.class)
    @Feature("API")
    @Story("Register")
    @Description("POST request for User Register")
    public void registerUser(String name, String email, String password) throws Exception {
        String body = postForm(REGISTER, Map.of("name", name, "email", email, "password", password), 201, null
        );
        Assert.assertTrue(body.contains("success") || body.contains("id") || body.contains("token"), "Unexpected body: " + body);
    }

    @Test(groups = {"Sanity"}, dataProvider = "loginUser", dataProviderClass = LoginDataProviders.class)
    @Feature("API")
    @Story("Login")
    @Description("POST request for User Login")
    public void loginUser(String email, String password) throws Exception {
        String body = postForm(
                LOGIN,
                Map.of("email", email, "password", password),
                200, null
        );
        Assert.assertTrue(body.contains("success") || body.contains("id") || body.contains("token"),
                "Unexpected body: " + body);
    }
}