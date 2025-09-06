package drivers;

import configurations.MobilePlatform;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public class MobileDriverManager {
    private static final ThreadLocal<AppiumDriver> TL = new ThreadLocal<>();

    public static AppiumDriver get() {
        return TL.get();
    }

    public static void start() {
        if (TL.get() != null) return;
        MobilePlatform platform = MobilePlatform.fromSysProp();
        String runEnv = System.getProperty("runEnv", "local");
        try {
            DesiredCapabilities caps = MobileCapabilitiesFactory.build(platform, runEnv);
            URL hub = new URL(MobileCapabilitiesFactory.hubUrl(runEnv));
            AppiumDriver driver = (platform == MobilePlatform.IOS)
                    ? new IOSDriver(hub, caps)
                    : new AndroidDriver(hub, caps);
            TL.set(driver);
        } catch (Exception e) {
            throw new RuntimeException("Failed to start mobile driver", e);
        }
    }

    public static void quit() {
        AppiumDriver d = TL.get();
        if (d != null) {
            d.quit();
            TL.remove();
        }
    }

    public static void quitIfAny() {
        MobileDriverManager.quit();
    }

}
