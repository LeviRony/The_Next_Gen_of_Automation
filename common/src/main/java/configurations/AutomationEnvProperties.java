package configurations;

public class AutomationEnvProperties {

    // @INFO: Here you need to set the automation running environment Staging or Production
    public static final String ENV_TYPE = System.getProperty("tests.general.envType", "PROD");
    // Options are: PROD/STG


}
