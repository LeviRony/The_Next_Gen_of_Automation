# Backend API & DataBase Module
Contains API tests using REST-assured.

Sample API Test:

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

---


# DataBase
Properties files path for DBs
```path
../common/src/main/resources/db-ci.properties
```

## How you run it
Locally
#### choose env via JVM prop (preferred)
> $ mvn -Denv=stg -pl backend -am test

or

> $ mvn -Denv=ci -pl backend -am test


### Git Action Files
> .github/workflows/db-ci.yml