package drivers;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URI;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Map;

public class MobileCapabilities {
    protected static final String PLATFORM =
            System.getProperty("platform", "android");
    protected static final String RUN_ENV =
            System.getProperty("runEnv", "local");
    private AppiumDriver driver;

    @SuppressWarnings("unchecked")
    private static Map<String, Object> loadJson(String name) {
        try (InputStream is = MobileCapabilities.class.getResourceAsStream("/configs/" + name)) {
            if (is == null) {
                throw new IllegalStateException("Config file not found on classpath: /configs/" + name);
            }
            return new ObjectMapper().readValue(is, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load mobile capabilities config: " + name, e);
        }
    }

    @SuppressWarnings("unchecked")
    public static DesiredCapabilities build(String platform, String runEnv) {
        String env = runEnv == null ? "" : runEnv.trim();
        String envKey = env.isEmpty() ? "local" : env;
        String cfgName = switch (envKey.toLowerCase()) {
            case "bstack" -> "mobile-browserstack.json";
            case "slabs" -> "mobile-saucelabs.json";
            default -> "mobile-local.json";
        };

        Map<String, Object> cfg = loadJson(cfgName);

        DesiredCapabilities caps = new DesiredCapabilities();
        Object baseObj = cfg.get("base");
        if (!(baseObj instanceof Map)) {
            throw new IllegalStateException("'base' section missing or invalid in " + cfgName);
        }
        ((Map<String, Object>) baseObj).forEach(caps::setCapability);

        String platformKey = (platform != null && !platform.isBlank() ? platform : System.getProperty("tests.general.mobileType", "android")).toLowerCase();
        Object platObj = cfg.get(platformKey);
        if (!(platObj instanceof Map)) {
            throw new IllegalArgumentException(
                    "Platform section '" + platformKey + "' not found in " + cfgName +
                            ". Available sections: " + cfg.keySet()
            );
        }
        ((Map<String, Object>) platObj).forEach(caps::setCapability);

        System.getProperties().forEach((k, v) -> {
            String key = String.valueOf(k);
            if (key.startsWith("cap.")) {
                caps.setCapability(key.substring(4), v);
            }
        });

        return caps;
    }

    public static String hubUrl(String runEnv) {
        String env = runEnv == null ? "" : runEnv.trim().toLowerCase();
        return switch (env) {
            case "bstack" -> "https://hub.browserstack.com/wd/hub";
            case "slabs" -> "https://ondemand.saucelabs.com/wd/hub";
            default -> System.getProperty("appiumUrl", "http://127.0.0.1:4723/");
        };
    }

    public static io.appium.java_client.AppiumDriver get() {
        return MobileDriverManager.get();
    }

    /**
     * Legacy quit alias expected by older tests (e.g., TestBaseMobile calls MobileCapabilities.q()).
     * Delegates to MobileDriverManager.quit().
     */
    public static void q() {
        MobileDriverManager.quit();
    }

    public void start() throws Exception {
        DesiredCapabilities caps = build(PLATFORM, RUN_ENV);
        URL hub = URI.create(hubUrl(RUN_ENV)).toURL();

        AppiumDriver driver;
        if ("ios".equals(PLATFORM)) {
            driver = new IOSDriver(hub, caps);
        } else {
            driver = new AndroidDriver(hub, caps);
        }
        this.driver = driver;
    }

    public AppiumDriver getDriver() {
        return driver;
    }

    public void stop() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}