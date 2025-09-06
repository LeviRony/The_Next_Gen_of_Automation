package allureReport;
import io.qameta.allure.Step;

public final class AllureLogger {
    @Step("{message}")
    public static void step(String message, Object... args) {}
}