# Automation Parent (Playwright + Appium + TestNG + Allure)

[![Maven](https://img.shields.io/badge/Maven-3.8+-blue)](https://maven.apache.org/)
[![Java](https://img.shields.io/badge/Java-17+-orange)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)

---

## Multi-module Maven project:

- [`./common/`](./common/) – Shared Playwright setup, BaseTest, config
- [`./backend/`](./backend/) – API tests with Playwright
- [`./frontend/`](./frontend/) – Web UI tests
- [`./mobile/`](./mobile/) – Mobile device tests

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
│                     ├─ allureReport/
│                     ├─ configurations/
│                     ├─ db/
│                     ├─ drivers/
│                     ├─ dataProviders/
│                     ├─ pageObjects/
│                     └─ utilities/
│
├─ frontend-module
│   ├─ pom.xml
│   ├─ jenkinsFile
│   ├─ README.md 
│   ├─ src/test/resources/test-suite.xml
│   └─ src/test/java/webName/test.java
│
├─ mobile-module
│   ├─ pom.xml
│   ├─ jenkinsFile
│   ├─ README.md
│   ├─ src/test/resources/test-suite.xml
│   └─ src/test/java/com/appName/test.java
│
│
├─ performance-module
│   ├─ pom.xml
│   ├─ jenkinsFile
│   ├─ README.md
│   ├─ src/test/resources/test-suite.xml
│   └─ src/test/java/com/appName/test.java
│
│
└─ backend-module
    ├─ pom.xml
    ├─ jenkinsFile
    ├─ src/test/resources/test-suite.xml
    └─ src/test/java/
```


## Dependency Management
All projects dependencies are heirs versions from main POM.xml
> ```<dependencyName.version>x.x.x</dependency.version>```

The modules projects calling versions from main POM
> ```<version>${dependencyName.version}</version>```




---

## 🚀 Getting Started

Ensure you have the following installed:

- Java 17+
- Maven 3.8+
- Node.js (for frontend tests, if applicable)
- PlayWright
- Appium + Android/iOS SDKs (for mobile tests)
- Git
- JMeter
- AI MCP
- Allure CLI (for report generation)


---
## Allure report

1) Generate results by running tests (Allure results in `**/allure-results`).
2) Generate aggregated Allure report:

```
mvn io.qameta.allure:allure-maven:report
or
mvn allure:report
```

The HTML report will be in `target/site/allure-maven-plugin` under each module. Open `index.html` in a browser.
---
### Notes

- Configure `src/main/resources/config.properties` in `common` or pass JVM properties:
    - `-Dheadless=false` to see the browser.
- Update devices or base URLs as needed.
---
## Quick Start

```bash
# Build all
mvn -q -DskipTests install

# Sanity on API+UI (PR/CI fast)
mvn -q -Psanity -pl api-tests,ui-tests test

# Full regression (staging)
mvn -q -am -pl frontend, backend, mobile -Dgroups=Regression test

# Run a single test
mvn -q -pl ui-tests -Dtest=LoginSmokeTest test

```
---
## Test Groups

- `Sanity`
- `Regression`
- `Negative`
- `Preformance`
- `Load`

> Configure base URLs and options via system properties or environment variables (see `core/Env.java`).

## CI - Jenkins 
Here you can field all the relevant information for connection and running the automation via CI.
The CI file (JenkinsFile) must run a Test-Suite and not a single tests

**Jenkins file path:** *../JenkinsFile*
**Machine OS:** *Windows, macOS*

See `Jenkinsfile` for a full CI pipeline (E2E/Allure).

## GitHub Actions CI

Workflow`.github/workflows/ci.yml`:

- PR → Smoke (API+UI)
- push to main → E2E full Sanity, Regression and Negative tests suites
- artifacts: Surefire + Allure results, Allure HTML

## 🔍 Code Review Checklist
When reviewing code, please check the following:

- Functionality
Does the code work as expected? Are all new features or fixes tested?

- Readability
Is the code easy to understand? Are variable/method names clear and meaningful?

- Consistency
Does the code follow the project’s style and conventions?

- Maintainability
Is the code modular and reusable? Are methods/classes well-structured and not too large?

- Tests
Are there sufficient automated tests? Do tests cover positive and negative cases?

- Documentation
Are complex parts documented? Are public methods/classes properly commented?

- Performance
Are there any obvious performance issues or inefficient algorithms?

- Security
Are inputs validated? Are there any security concerns?

- Dependencies
Are new dependencies necessary and appropriate?

- Build & CI
Does the code build without errors? Does it pass CI checks?

---
## Format

```bash
mvn -q spotless:check

#or to auto-fix:
mvn -q spotless:apply

# per-module:
mvn -q -pl backend -am spotless:check
```

---
# User Input 
Function that is allow you to get inputs from user console 
UserInput.class

---

## Git Package
You'll need to update your GitHub uesrName + create Token

Local Development Setup (on your laptop)
Generate a Personal Access Token (PAT) on GitHub:
Go to GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
.

Click Generate new token (classic).
Give it a name like maven-deploy.
Scopes needed:
> write:packages 
> read:packages
> repo

Copy the token (you’ll only see it once).

Create (or edit) 
```path
~/.m2/settings.xml
```
Path:
```path 
Linux/Mac: ~/.m2/settings.xml / open -a TextEdit ~/.m2/settings.xml

Windows: C:\Users\<you>\.m2\settings.xml
```
```xml
<settings>
  <servers>
    <server>
      <id>github</id> <!-- must match <id> in pom.xml -->
      <username>GITHUB_USERNAME</username>
      <password>YOUR_PERSONAL_ACCESS_TOKEN</password>
    </server>
  </servers>
</settings>
 ```
---



## License
This project is licensed under the MIT License – see the [LICENSE](LICENSE) file for details.