# Automation Parent (Playwright + TestNG + Allure)

[![Maven](https://img.shields.io/badge/Maven-3.8+-blue)](https://maven.apache.org/)
[![Java](https://img.shields.io/badge/Java-17+-orange)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)

A **multi-module Maven automation project** supporting **Web**, **Mobile**, and **API** testing. Designed for *
*modularity**, **scalability**, and **reusability**.
---

## Multi-module Maven project:
- `common-module` – shared Playwright setup, BaseTest, config
- `backend-module` – API tests with Playwright
- `frontend-module` – Web UI tests
- `mobile-module` – device emulation test

---

## Project Structure

```
automation-project
│
├─ pom.xml ← Parent POM
├─ jenkinsFile 
│ 
├─ common-module
│   ├─ pom.xml
│   ├─ jenkinsFile
│   ├─ README.md
│   └─ src/main/java/com/
│                     ├─ configurations/
│                     ├─ dataProviders/
│                     ├─ pageObjects/
│                     └─ utilities/
│
├─ frontend-module
│   ├─ pom.xml
│   ├─ jenkinsFile
│   ├─ README.md 
│   └─ src/test/java/webName/
│
├─ mobile-module
│   ├─ pom.xml
│   ├─ jenkinsFile
│   ├─ README.md
│   └─ src/test/java/com/appName/
│
└─ backend-module
    ├─ pom.xml
    ├─ jenkinsFile
    └─ src/test/java/
```

---




---


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