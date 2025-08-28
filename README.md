# Automation Parent (Playwright + TestNG + Allure)

Multi-module Maven project:
- `common` – shared Playwright setup, BaseTest, config
- `backend` – API tests with Playwright
- `frontend` – Web UI tests
- `mobile` – device emulation test


## Prerequisites
- Java 17+
- Maven 3.9+


## First run (install Playwright browsers)
From the project root:
```bash
mvn -q -pl :common exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install --with-deps"
```


## Run tests
```bash
# Run per module:

mvn -pl web test
mvn -pl mobile test
mvn -pl api test

# Run all modules:
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


## Quick Start
```bash
# Build all
mvn -q -DskipTests install

# Sanity on API+UI (PR/CI fast)
mvn -q -Psanity -pl api-tests,ui-tests test

# Full regression (staging)
mvn -q -Pregression -pl api-tests,ui-tests,e2e-tests test

# Run a single test
mvn -q -pl ui-tests -Dtest=LoginSmokeTest test

# Perf (requires k6 installed on runner)
cd perf-tests && k6 run scripts/ingest.k6.js
```

> Configure base URLs and options via system properties or environment variables (see `core/Env.java`).

See `Jenkinsfile` for a full CI pipeline (E2E/Allure).



## GitHub Actions CI
Workflow`.github/workflows/ci.yml`:
- PR → Smoke (API+UI)
- push to main → E2E full Sanity, Regression and Negative tests suites
- artifacts: Surefire + Allure results, Allure HTML

## Test Groups
- `Sanity` 
- `Regression`
- `Negative`
- `Preformance`
- `Load`