package drivers;

import com.fasterxml.jackson.databind.ObjectMapper;
import configurations.MobilePlatform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.InputStream;
import java.util.Map;

public class MobileCapabilitiesFactory {
    private static Map<String, Object> loadJson(String name) {
        try (InputStream is = MobileCapabilitiesFactory.class.getResourceAsStream("/configs/" + name)) {
            return new ObjectMapper().readValue(is, Map.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static DesiredCapabilities build(MobilePlatform platform, String runEnv) {
        Map<String, Object> cfg = switch (runEnv) {
            case "bStack" -> loadJson("mobile-browserstack.json");
            case "sLabs" -> loadJson("mobile-saucelabs.json");
            default -> loadJson("mobile-local.json");
        };
        DesiredCapabilities caps = new DesiredCapabilities();
        Map<String, Object> base = (Map<String, Object>) cfg.get("base");
        base.forEach(caps::setCapability);

        Map<String, Object> plat = (Map<String, Object>) cfg.get(platform == MobilePlatform.IOS ? "ios" : "android");
        plat.forEach(caps::setCapability);

        System.getProperties().forEach((k, v) -> {
            String key = String.valueOf(k);
            if (key.startsWith("cap.")) caps.setCapability(key.substring(4), v);
        });
        return caps;
    }

    public static String hubUrl(String runEnv) {
        return switch (runEnv) {
            case "bStack" -> "https://hub.browserstack.com/wd/hub";
            case "sLabs" -> "https://ondemand.saucelabs.com/wd/hub";
            default -> System.getProperty("appiumUrl", "http://127.0.0.1:4723/");
        };
    }
}