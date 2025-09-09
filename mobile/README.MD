# Mobile Module

## Mobile Configuration

This project supports multiple mobile platforms (android, ios) and different execution environments (local, bStack,
sLabs).
The active values are controlled by the platform and runEnv system properties, which are defined in the pom.xml and
passed into the JVM for test execution.
The active environment is controlled by the tests.general.envType system property, which flows into the Java code:

```path 
../common/src/main/java/drivers/MobileCapabilities.java
```

```java
protected static final String PLATFORM =
        System.getProperty("platform", "android");
protected static final String RUN_ENV =
        System.getProperty("runEnv", "local");
```

```xml
<!-- in main pom.xml -->
<platform>android</platform>
<runEnv>local</runEnv>
```

In your Java code, you can read them the same way as tests.general.envType:

```java
protected static final String PLATFORM =
        System.getProperty("platform", "android");

protected static final String RUN_ENV =
        System.getProperty("runEnv", "local");
```

#### Default Behavior

If no property is passed, the defaults from the POM are used:

* platform=android (options: android, ios)
* runEnv=local     (options:  local, bStack, sLabs)
  Override at Runtime

You can override either or both when running Maven:

```bash
# Run on iOS locally
mvn -Dplatform=ios -DrunEnv=local test

# Run on Android in BrowserStack
mvn -Dplatform=android -DrunEnv=bStack test
```

#### In CI/CD pipelines

Always use the **override option** in your pipeline definitions to make sure the correct environment is targeted:

```bash
mvn -Dplatform=ios -DrunEnv=local test
```

### Examples

Android local:

```bash
mvn -pl mobile -am test \
  -Dplatform=android \
  -DrunEnv=local \
  -DappiumUrl=http://127.0.0.1:4723/ \
  -Dcap.deviceName="Pixel_7_API_34" \
  -Dcap.app="/Users/you/apps/demo.apk"
```

iOS local:

```bash
mvn -pl mobile -am test \
  -Dplatform=ios -DrunEnv=local \
  -Dcap.deviceName="iPhone 15" \
  -Dcap.app="/Users/you/apps/Demo.app"
```

BrowserStack:

```bash
export BROWSERSTACK_USERNAME=...; export BROWSERSTACK_ACCESS_KEY=...
mvn -pl mobile -am test -Dplatform=android -DrunEnv=bs \
  -Dcap."bstack:options".userName=$BROWSERSTACK_USERNAME \
  -Dcap."bstack:options".accessKey=$BROWSERSTACK_ACCESS_KEY
```