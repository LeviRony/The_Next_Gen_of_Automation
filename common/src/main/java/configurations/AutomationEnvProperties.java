package configurations;


enum EnvironmentType {
  PROD, STG , DEV, QA
}
public class AutomationEnvProperties {

  public static final EnvironmentType ENV_TYPE =
          EnvironmentType.valueOf(System.getProperty("tests.general.envType", "PROD"));
}
