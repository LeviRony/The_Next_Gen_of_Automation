package drivers;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public class MobileDriverManager {
    private static final ThreadLocal<AppiumDriver> THREAD_LOCAL = new ThreadLocal<>();

    public static AppiumDriver get() {
        return THREAD_LOCAL.get();
    }

    public static void start() {
        if (THREAD_LOCAL.get() != null) return;
        String platform = System.getProperty("tests.general.mobileType", "android").toLowerCase();
        String runEnv = System.getProperty("runEnv", "local");
        try {
            DesiredCapabilities caps = MobileCapabilities.build(platform, runEnv);
            URL hub = new URL(MobileCapabilities.hubUrl(runEnv));
            AppiumDriver driver;
            if ("ios".equals(platform)) {
                driver = new IOSDriver(hub, caps);
            } else {
                driver = new AndroidDriver(hub, caps);
            }
            THREAD_LOCAL.set(driver);
        } catch (Exception e) {
            throw new RuntimeException("Failed to start mobile driver", e);
        }
    }

    public static void quit() {
        AppiumDriver d = THREAD_LOCAL.get();
        if (d != null) {
            d.quit();
            THREAD_LOCAL.remove();
        }
    }
}