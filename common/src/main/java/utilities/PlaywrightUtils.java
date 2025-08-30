package utilities;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Step;

public class PlaywrightUtils {

    @Step
    public static void navigateTo(Page page, String uri) {
        page.navigate(uri);
        System.out.println("\u001B[34m[INFO] Navigated to: " + uri + "\u001B[0m");
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    public static void clickInsideIframe(Page page, String frameLocator, String elementLocator) {
        FrameLocator frame = page.frameLocator(frameLocator);
        frame.locator(elementLocator).click();
    }

    public static boolean isIframeElementVisible(Page page, String frameLocator, String elementLocator) {
        FrameLocator frame = page.frameLocator(frameLocator);
        return frame.locator(elementLocator).isVisible();
    }
}
