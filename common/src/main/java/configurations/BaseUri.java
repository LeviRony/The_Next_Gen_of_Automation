package configurations;

public class BaseUri {

    public static String urlPractice() {
        String host = null;
        if (AutomationEnvProperties.ENV_TYPE.equals("STG")) {
            host = "https://practice.stag.expandtesting.com/";
        } else if (AutomationEnvProperties.ENV_TYPE.equals("PROD")) {
            host = "https://practice.expandtesting.com/";
        }
        return host;
    }

    /**
     * Use this im case of more than 2 conditions
     * Don't forget to update the AutomationEnvProperties options
     */

    public static String urlPracticeTest2() {
        return switch (AutomationEnvProperties.ENV_TYPE) {
            case DEV  -> "https://practice.dev.expandtesting.com/";
            case QA   -> "https://practice.qa.expandtesting.com/";
            case STG  -> "https://practice.stag.expandtesting.com/";
            case PROD -> "https://practice.expandtesting.com/";
        };
    }
}
