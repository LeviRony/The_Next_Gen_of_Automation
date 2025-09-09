package configurations;


public class BaseUri {

    protected static final String ENV_TYPE =
            System.getProperty("tests.general.envType", "STG");
    // Options are: DEV | QA | STG | PROD

    public static String urlPractice() {
        String host = null;
        System.out.println("envType " + ENV_TYPE);
        if (ENV_TYPE.equals("STG")) {
            host = "https://practice.stag.expandtesting.com/";
        } else if (ENV_TYPE.equals("PROD")) {
            host = "https://practice.expandtesting.com/";
        }
        return host;
    }

    /**
     * Use this im case of more than 2 conditions
     * Don't forget to update the AutomationEnvProperties options
     */
    public static String urlDbPracticeTest() {
        return switch (ENV_TYPE) {
            case "DEV" -> "https://practice.dev.expandtesting.com/";
            case "QA" -> "https://practice.qa.expandtesting.com/";
            case "STG" -> "https://practice.stag.expandtesting.com/";
            case "PROD" -> "https://practice.expandtesting.com/";
            default -> throw new IllegalStateException("Unexpected value: " + ENV_TYPE);
        };
    }
}
