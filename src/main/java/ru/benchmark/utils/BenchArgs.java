package ru.benchmark.utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.Arrays;
import java.util.stream.Collectors;

@Value
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BenchArgs {

    private static final String DELIMITER = "@";

    String[] args;

    public static BenchArgs of(String args) {
        return new BenchArgs(args.split(DELIMITER));
    }

    public static BenchArgs of(String[] args) {
        return new BenchArgs(args);
    }

    public String toString() {
        return String.join(DELIMITER, args);
    }
}
