package cuccovillo.alessio.jtaillib.enums;

/**
 *
 * @author alessio
 */
public enum CommandLineOption {
    FOLLOW("f", "follow", false, "output appended data as the file grows;"),
    LINES("n", "lines", true, "output the last NUM lines, instead of the last 10 [or use -n +NUM to output starting with line NUM];"),
    SLEEP("s", "sleep-interval", true, "with -f, sleep for approximately N seconds (default 1.0) between iterations;"),
    VERBOSE("v", null, false, "always output headers giving file names;"),
    HELP("h", "help", false, "display this help and exit;"),
    GUI(null, "gui", false, "open GUI instead work on terminal");

    private final String opt;
    private final String longOpt;
    private final boolean hasArg;
    private final String description;

    private CommandLineOption(String opt, String longOpt, boolean withArg, String description) {
        this.opt = opt;
        this.longOpt = longOpt;
        this.hasArg = withArg;
        this.description = description;
    }

    public String getOpt() {
        return opt;
    }

    public String getLongOpt() {
        return longOpt;
    }

    public boolean hasArg() {
        return hasArg;
    }

    public String getDescription() {
        return description;
    }

}
