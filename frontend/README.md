# # Frontend WebUI Module - Playwright_project

This module contains Web UI tests written with Playwright for Java.
It follows the Page Object Model (POM) pattern for maintainability.
---

## Prerequisites

* Java 17+
* Maven 3.9+
* Node.js (for Playwright drivers)
* Browsers installed via Playwright (mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install")

---

### Example Page Object:

```java
public class LoginPage {
    private WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterUsername(String username) {
        driver.findElement(By.id("username")).sendKeys(username);
    }

    public void enterPassword(String password) {
        driver.findElement(By.id("password")).sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(By.id("loginBtn")).click();
    }
}
```

---

### Sample Test:

```java

@Test
public void testLogin() {
    LoginPage loginPage = new LoginPage(driver);
    loginPage.enterUsername("user");
    loginPage.enterPassword("pass");
    loginPage.clickLogin();
}

```

### Running Tests

```bash
mvn -pl frontend -am test
```

Override environment or browser if needed:

```bash
mvn -pl frontend -am test -Denv=stg -Dbrowser=chromium
```

---

# Playwright Tools

### Codegen (Test Recorder)

Use the codegen command to run the test generator, followed by the URL of the website you want to generate tests for.
The URL is optional, and you can always run the command without it and then add the URL directly into the browser window
instead.

### Installing Codegen

```
 mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install" 
```

### Running Codegen

```
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="codegen demo.playwright.dev/todomvc"
```

### Opening Trace Viewer

You can open a saved trace using either the Playwright CLI or in the browser at trace.playwright.dev. Make sure to add
the full path to where your trace.zip file is located.

``` 
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="show-trace trace.zip"
``` 

Open also in browser: trace.playwright.dev.