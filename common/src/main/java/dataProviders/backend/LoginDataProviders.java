package dataProviders.backend;

import org.testng.annotations.DataProvider;

public class LoginDataProviders {

    @DataProvider(name = "registerUser")
    public Object[][] registerUser() {
        return new Object[][]{
                {"Rony+024", "rony56@test.com", "test123"},
                {"Alex24", "alex6+@test.com", "s123"},
                {"R5", "a2@a.", "123456"}
        };
    }

    @DataProvider(name = "loginUser")
    public Object[][] loginUser() {
        return new Object[][]{
                {"rony56@test.com", "test123"},
                {"alex6+@test.com", "s123"},
                {"a2@a.", "123456"}
        };
    }
}