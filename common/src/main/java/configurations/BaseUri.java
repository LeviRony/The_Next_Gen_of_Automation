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
}
