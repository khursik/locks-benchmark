package ru.somepackage.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class BenchArgs {

    private static final String DELIMITER = "@";
    private final String[] args;

    protected BenchArgs(String[] args) {
        this.args = args;
    }

    public String[] get() {
        return args;
    }

    public static BenchArgs of(String args) {
        return new BenchArgs(args.split(DELIMITER));
    }

    public static BenchArgs of(String[] args) {
        return new BenchArgs(args);
    }

    public String toString() {
        return Arrays.stream(args).collect(Collectors.joining(DELIMITER));
    }
}
