# Backend Module – API & Database Testing

This module contains:

* **API** tests using REST-assured.
* **Database** integration tests (Postgres/MySQL/etc.), with environment-specific configs stored in the common module.

## Prerequisites

* Java 17+
* Maven 3.9+
* A running API backend (or mock server)
* Database access credentials (configured via properties or environment variables)

---

## API Test:

```java

@Test
public void getUserTest() {
    given()
            .baseUri(ConfigReader.getProperty("api.baseUrl"))
            .when()
            .get("/users/1")
            .then()
            .statusCode(200)
            .body("id", equalTo(1));
}

```

* ConfigReader resolves values from config.properties or JVM/system properties.
* Base URLs can be overridden at runtime:

```bash
mvn -pl backend -am test -Dapi.baseUrl=https://stg.api.example.com
```

---

# DataBase

Properties are defined in the common module:

```path
../common/src/main/resources/db-ci.properties
```

Typical keys:

```properties
db.url=jdbc:postgresql://localhost:5432/app
db.user=app_user
db.pass=${DB_PASS}
db.schema=public
```

DB config is loaded via DbConfig.load(env) and injected into DbClient.

---

## Running Tests

Local Run

#### Choose environment via JVM property:

```bash
# Staging
mvn -Denv=stg -pl backend -am test

# CI
mvn -Denv=ci -pl backend -am test
```

---

### Git Action Files

Workflow: .github/workflows/db-ci.yml

Runs DB + API sanity checks on pull requests and pushes.