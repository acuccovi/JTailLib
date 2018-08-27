package cuccovillo.alessio.jtaillib;

import cuccovillo.alessio.jtaillib.enums.CommandLineOption;
import java.io.File;
import java.io.Serializable;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author alessio
 */
public class Settings implements Serializable {

    static final long serialVersionUID = 1L;
    private boolean follow;
    private int lines = 10;
    private long sleep = 1000L;
    private boolean verbose;
    private boolean gui;

    private File file;

    private final CommandLine commandLine;

    public static Settings defaultSettings(File file) {
        Settings settings = null;
        try {
            settings = new Settings(file);
        } catch (ParseException ignore) {
        }
        return settings;
    }

    private Settings(File file) throws ParseException {
        this("--gui");
        this.file = file;
    }

    public Settings(String... args) throws ParseException {
        Options options = createOptions();
        CommandLineParser parser = new DefaultParser();
        commandLine = parser.parse(options, args);

        showHelp(options);

        gui = commandLine.hasOption(CommandLineOption.GUI.getLongOpt());
        follow = gui ? true : commandLine.hasOption(CommandLineOption.FOLLOW.getOpt());
        verbose = gui ? false : commandLine.hasOption(CommandLineOption.VERBOSE.getOpt());
        if (commandLine.hasOption(CommandLineOption.LINES.getOpt())) {
            String linesValue = commandLine.getOptionValue(CommandLineOption.LINES.getOpt());
            try {
                lines = Integer.parseInt(commandLine.getOptionValue(CommandLineOption.LINES.getOpt()));
            } catch (NumberFormatException nfe) {
                System.err.printf("jtail: invalid number of lines: ‘%s’", linesValue);
            }
        }
        if (commandLine.hasOption(CommandLineOption.SLEEP.getOpt())) {
            String sleepValue = commandLine.getOptionValue(CommandLineOption.SLEEP.getOpt());
            try {
                sleep = Long.parseLong(sleepValue) * 1000L;
            } catch (NumberFormatException nfe) {
                System.err.printf("jtail: invalid number of seconds: ‘%s’%n", sleepValue);
            }
        }
        if (!gui) {
            checkArgs();
            file = new File(commandLine.getArgs()[0]);
        }
    }

    private void showHelp(Options options) {
        if (commandLine.hasOption(CommandLineOption.HELP.getOpt())) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("JTail", options, true);
            System.exit(0);
        }
    }

    private void checkArgs() {
        if (commandLine.getArgs().length == 0) {
            System.out.println("No file provided");
            System.exit(-1);
        }
    }

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }

    public int getLines() {
        return lines;
    }

    public void setLines(int lines) {
        this.lines = lines;
    }

    public long getSleep() {
        return sleep;
    }

    public void setSleep(long sleep) {
        this.sleep = sleep;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isGUI() {
        return gui;
    }

    private Options createOptions() {
        Options options = new Options();
        //FOLLOW
        options.addOption(CommandLineOption.FOLLOW.getOpt(),
                CommandLineOption.FOLLOW.getLongOpt(),
                CommandLineOption.FOLLOW.hasArg(),
                CommandLineOption.FOLLOW.getDescription());
        //LINES
        options.addOption(CommandLineOption.LINES.getOpt(),
                CommandLineOption.LINES.getLongOpt(),
                CommandLineOption.LINES.hasArg(),
                CommandLineOption.LINES.getDescription());
        options.getOption(CommandLineOption.LINES.getOpt())
                .setArgName("NUM");
        //SLEEP
        options.addOption(CommandLineOption.SLEEP.getOpt(),
                CommandLineOption.SLEEP.getLongOpt(),
                CommandLineOption.SLEEP.hasArg(),
                CommandLineOption.SLEEP.getDescription());
        options.getOption(CommandLineOption.SLEEP.getOpt())
                .setArgName("N");
        //VERBOSE
        options.addOption(CommandLineOption.VERBOSE.getOpt(),
                CommandLineOption.VERBOSE.getLongOpt(),
                CommandLineOption.VERBOSE.hasArg(),
                CommandLineOption.VERBOSE.getDescription());
        //HELP
        options.addOption(CommandLineOption.HELP.getOpt(),
                CommandLineOption.HELP.getLongOpt(),
                CommandLineOption.HELP.hasArg(),
                CommandLineOption.HELP.getDescription());
        //GUI
        options.addOption(CommandLineOption.GUI.getOpt(),
                CommandLineOption.GUI.getLongOpt(),
                CommandLineOption.GUI.hasArg(),
                CommandLineOption.GUI.getDescription());
        return options;
    }

}
