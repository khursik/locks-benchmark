package ru.jmh.utils;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArgsParser {

    private static final Logger LOG = LoggerFactory.getLogger(ArgsParser.class);
    private final CommandLine commandLine;

    public ArgsParser(String[] args) throws ParseException {
        Option typeOption = Option.builder("t")
                .required(false)
                .desc("Type of lock, such as: TAS, TTAS, Backoff, CLH, MCS")
                .hasArg()
                .longOpt("type")
                .build();
        Option threadsCountOption = Option.builder("thc")
                .required(false)
                .desc("Threads count")
                .hasArg()
                .longOpt("threadsCount")
                .build();
        Option timeoutOption = Option.builder("tms")
                .required(false)
                .desc("Timeout milliseconds")
                .hasArg()
                .longOpt("timeoutMs")
                .build();
        Option helpOption = Option.builder("h")
                .required(false)
                .desc("Print help")
                .hasArg()
                .longOpt("help")
                .build();
        Options options = new Options();
        options.addOption(typeOption);
        options.addOption(threadsCountOption);
        options.addOption(timeoutOption);
        CommandLineParser parser = new DefaultParser();
        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException exception) {
            LOG.error("Cannot parse input args", exception);
            throw exception;
        }
        if (commandLine.hasOption('h')) {
            printHelpString(options);
        }
    }

    public String getLockType() {
        return commandLine.getOptionValue('t');
    }

    public int getThreadsCount() {
        return Integer.parseInt(commandLine.getOptionValue("thc"));
    }

    public long getTimeoutMs() {
        return Long.parseLong(commandLine.getOptionValue("tms"));
    }

    private void printHelpString(Options options) {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("java -jar locks-benchmark-1.0.jar", options);
    }
}
