## 1️Common Module - Projects Configurations

## Prerequisites

- Java 17+
- Maven 3.9+

---

## Contents

* allureReport/ → Allure reporting hooks and custom loggers
* configurations/ → Environment setup (URLs, properties, enums)
* db/ → Database clients, configs, test base classes
* drivers/ → Playwright/Appium/Selenium driver factories
* dataProviders/ → TestNG data providers
* pageObjects/ → Base Page classes and helpers for UI automation
* utilities/ → Common utilities (assertions, waiters, file I/O, etc.)

---

## Dependency

All other modules (backend, frontend, mobile, db, performance) declare a dependency on common in their pom.xml:

```xml

<dependency>
    <groupId>io.github.levirony</groupId>
    <artifactId>common</artifactId>
    <version>${project.version}</version>
</dependency>
```

---

## Usage

* Extend BaseTest for Playwright/Selenium/Appium setup.
* Use configurations.Env or AutomationEnvProperties for environment switching (-Denv=qa|stg|prod).
* Use AllureLogger.step("...") for adding steps into Allure reports.
* Import DbConfig / DbClient for DB testing.
