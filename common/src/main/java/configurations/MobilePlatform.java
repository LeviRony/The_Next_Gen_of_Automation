package configurations;


public enum MobilePlatform {
    ANDROID, IOS;

    public static MobilePlatform fromSysProp() {
        String v = System.getProperty("platform", "android").toLowerCase();
        return v.startsWith("ios") ? IOS : ANDROID;
        // Options: android / ios
    }
}
