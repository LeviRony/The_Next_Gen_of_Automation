package configurations;


/**
 * Central environment knobs for mobile + AltTester.
 */
public class MobileEnv {

    // ---- Property keys (single source of truth) ----
    public static final String PROP_ENV = "tests.general.envType";
    public static final String PROP_DEVICE = "device";



    protected static final String DEVICE_TYPE =
            System.getProperty(PROP_DEVICE, "SM-F936B");
    // SM-F936B | Pixel_6_Pro_API_33

}