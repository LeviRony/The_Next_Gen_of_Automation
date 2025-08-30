package apiTest;

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
    String body = get(HEALTH_CHECK, 200, null, null);
    Assert.assertTrue(body.contains("Notes API is Running"), "ERROR");

    // @INFO: with cookie
    String body2 = get(HEALTH_CHECK, 200, Map.of(COOKIE_TYPE, COOKIE_KEY), null);
    Assert.assertTrue(body2.contains("Notes API is Running"), "ERROR");
  }

  @Test(
      groups = {"Regression"},
      dataProvider = "registerUser",
      dataProviderClass = LoginDataProviders.class)
  @Feature("API")
  @Story("Register")
  @Description("POST request without for User Register")
  public void registerUser(String name, String email, String password) throws Exception {
    String body =
        postForm(REGISTER, Map.of("name", name, "email", email, "password", password), 201, null);
    Assert.assertTrue(
        body.contains("success") || body.contains("id") || body.contains("token"),
        "Unexpected body: " + body);
  }

  @Test(
      groups = {"Sanity"},
      dataProvider = "loginUser",
      dataProviderClass = LoginDataProviders.class)
  @Feature("API")
  @Story("Login")
  @Description("POST request with cookie for User Login")
  public void loginUser(String email, String password) throws Exception {
    String body =
        postForm(
            LOGIN,
            Map.of("email", email, "password", password),
            200,
            Map.of(COOKIE_TYPE, COOKIE_KEY));
    Assert.assertTrue(
        body.contains("success") || body.contains("id") || body.contains("token"),
        "Unexpected body: " + body);
  }
}
