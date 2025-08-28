# Backend API Module
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
