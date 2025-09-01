package dataProviders.backend;

import org.testng.annotations.DataProvider;

import java.util.HashMap;
import java.util.Map;

public class LoginDataProviders {

    @DataProvider(name = "registerUser")
    public Object[][] registerUser() {
        return new Object[][]{
                // true / false for the assertType
                {Map.of("name", "R5", "email", "a2@a.", "password", "123456"),
                        400, "Invalid email", false},
                {Map.of("name", "Alex24", "email", "alex6+@test.com", "password", "s123"),
                        400, "Invalid password", false},
                {Map.of("name", "Rony+024", "email", "rony56@test.com", "password", "test123"),
                        409, "already exists", true},
                {Map.of("name", "BadUser", "email", "bad@test.com", "password", "123"),
                        400, "success", true}
        };
    }

    @DataProvider(name = "loginUser")
    public Object[][] loginUser() {
        return new Object[][]{
                {Map.of("email", "rony56@test.com", "password", "test123"), 200, "success", true},
                {Map.of("email", "alex6+@test.com", "password", "s123"), 400, "Invalid", false},
                {Map.of("email", "a2@a.", "password", "123456"), 400, "already exists", false}
        };
    }
}
