package utilities;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Page;

public class PlaywrightUtils {
    public static Page page;

    public PlaywrightUtils(Page page) {
        this.page = page;
    }

    public static void navigateTo(String uri) {
        page.navigate(uri);
        System.out.println("\u001B[34m[INFO] Navigated to: " + uri + "\u001B[0m");
    }

    public static void clickInsideIframe(String frameLocator, String elementLocator) {
        FrameLocator frame = page.frameLocator(frameLocator);
        frame.locator(elementLocator).click();
    }

    public static boolean isIframeElementVisible(String frameLocator, String elementLocator) {
        FrameLocator frame = page.frameLocator(frameLocator);
        return frame.locator(elementLocator).isVisible();
    }
}
