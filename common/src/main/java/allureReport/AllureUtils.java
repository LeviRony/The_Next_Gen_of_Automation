package allureReport;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;

/**
 * Common Allure utilities (screenshots, attachments, logs).
 */
public final class AllureUtils {

    private AllureUtils() {
        // prevent instantiation
    }

    /**
     * Attach a PNG screenshot to the Allure report.
     * Safe to call anytime — never fails the test if screenshot capture fails.
     *
     * @param driver the WebDriver or AppiumDriver implementing TakesScreenshot
     * @param name   the label for the screenshot
     */
    public static void attachScreenshot(Object driver, String name) {
        try {
            if (driver instanceof TakesScreenshot) {
                byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment(name, "image/png", new ByteArrayInputStream(png), ".png");
            } else {
                Allure.step("Driver does not support screenshots");
            }
        } catch (Exception e) {
            Allure.step("Could not capture screenshot: " + e.getMessage());
        }
    }

    /**
     * Attach text information to Allure (for logs, debug notes, etc.)
     */
    public static void attachText(String name, String message) {
        Allure.addAttachment(name, "text/plain", message);
    }
}