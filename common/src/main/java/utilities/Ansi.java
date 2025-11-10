package utilities;

public class Ansi {

    private Ansi() {
    }

    public static final String RESET = "\u001B[0m";  // reset all styles
    public static final String BOLD = "\u001B[1m";
    public static final String BOLD_OFF = "\u001B[22m"; // turns bold off only

    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String ORANGE = "\u001B[38;2;255;165;0m";
    public static final String GRAY = "\u001B[37m";
    public static final String WHITE = "\u001B[97m";

    // Example how to use colors log.info(RED + "This is red text" + RESET);

    // Usage: style("text", BOLD, RED) this will make text bold & colored
    static String style(String text, String... codes) {
        StringBuilder sb = new StringBuilder();
        for (String c : codes) sb.append(c);
        return sb.append(text).append(RESET).toString();
    }

}
