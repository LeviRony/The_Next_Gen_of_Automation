# Performance Module

This module contains JMeter performance tests for APIs and web endpoints.
Tests are executed via the jmeter-maven-plugin.
---

## Prerequisites

* Java 17+
* Maven 3.9+
* Apache JMeter (installed automatically by the plugin)

---

## Running Tests (JMeter)

From the project root:

```bash
mvn -pl performance -am verify \
  -Dthreads=5 \
  -Dduration=60 \
  -DrampUp=10 \
  -DbaseUrl=https://practice.expandtesting.com
```

Parameters:

* threads → number of virtual users
* duration → test duration in seconds
* rampUp → time to ramp up all users
* baseUrl → target environment

## JMeter Test Results

After execution, results and reports are generated in:

```path
performance/target/jmeter/reports/<test-name>/index.html
```

Example:

```path
performance/target/jmeter/reports/smoke-get/index.html
```

Open index.html in a browser to view graphs and metrics.

## Best Practices

* Keep .jmx test plans under performance/src/test/jmeter/.
* Use -DbaseUrl to switch between staging, QA, or prod endpoints.
* For CI pipelines (Jenkins/GitHub Actions), archive the HTML reports as build artifacts and publish them for easy
  viewing.
* Combine with Allure if you want unified reports across functional + performance modules.