# Automation Parent (Playwright + TestNG + Allure)

Multi-module Maven project:
- `_common` – shared Playwright setup, BaseTest, config
- `backend` – API tests with Playwright
- `frontend` – Web UI tests
- `mobile` – device emulation test


## Prerequisites
- Java 17+
- Maven 3.9+

## First run (install Playwright browsers)
From the project root:
```
mvn -q -pl :common exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install --with-deps"
```

## Run tests
Run per module:
```
mvn -pl web test
mvn -pl mobile test
mvn -pl api test
```
Run all modules:
```
 mvn test

```

## Allure report
1) Generate results by running tests (Allure results in `**/allure-results`).
2) Generate aggregated Allure report:
```
mvn io.qameta.allure:allure-maven:report
```
The HTML report will be in `target/site/allure-maven-plugin` under each module. Open `index.html` in a browser.

### Notes
- Configure `src/main/resources/config.properties` in `common` or pass JVM properties:
    - `-Dheadless=false` to see the browser.
- Update devices or base URLs as needed.
