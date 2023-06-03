package ru.somepackage;

import org.apache.commons.cli.ParseException;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import ru.somepackage.locks.*;
import ru.somepackage.utils.ArgsParser;
import ru.somepackage.utils.BenchArgs;
import ru.somepackage.utils.LockType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class LockTester {
    private static volatile long counter = 0;
    private static volatile Boolean stopFlag = false;

    public static void main(String[] args) throws BrokenBarrierException, ParseException, InterruptedException, RunnerException {
        warmUpJvm(args);
        runApp(args);
    }

    public static void warmUpJvm(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include("MainBenchmark")
                .param("args", BenchArgs.of(args).toString())
                .forks(0)
                .build();
        new Runner(opt).run();
    }

    public static int runApp(String[] args) throws InterruptedException, BrokenBarrierException, ParseException {
        long startTime = System.currentTimeMillis();
        System.out.println("jmh.Benchmark started in " + startTime);

        System.err.println(Arrays.stream(args).collect(Collectors.joining("|")));

        ArgsParser argsParser = new ArgsParser(args);
        int threadsCount = argsParser.getThreadsCount();
        String lockType = argsParser.getLockType().toUpperCase();
        long timeoutMs = argsParser.getTimeoutMs();
        System.out.printf("... with the following parameters: {\"lockType\": %s, \"nThreads\": %d, \"timeoutMs\": %d}.\n", lockType, threadsCount, timeoutMs);

        MyLock lock = getLockImp(LockType.valueOf(lockType));
        final CyclicBarrier gate = new CyclicBarrier(threadsCount + 1);


        List<Thread> threadsList = new ArrayList<>();
        for (int i = 0; i < threadsCount; i++) {
            threadsList.add(new Thread(() -> {
                try {
                    gate.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }

                while(!stopFlag) {
                    try {
                        lock.lock();
                        counter++;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } finally {
                        lock.unlock();
                    }
                }
                try {
                    gate.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
            }));
        }
        threadsList.forEach(Thread::start);

        try {
            gate.await();
            startTime = System.currentTimeMillis();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }

        Thread.sleep(timeoutMs);
        stopFlag = true;
        gate.await();
        long completeTime = System.currentTimeMillis();
        System.out.println("Complete incrementation in " + completeTime);
        long totalTime = completeTime - startTime;
        System.out.printf("Total time: %s ms\n", totalTime);
        System.out.println("Counter value: " + counter);
        counter = 0;
        stopFlag = false;
        return 0;
    }

    private static MyLock getLockImp(LockType lockType) {
        switch (lockType) {
            case TAS:
                return new TASLock();
            case TTAS:
                return new TTASLock();
            case BACKOFF:
                return new ExponentialBackoff();
            case CLH:
                return new CLHLock();
            case MCS:
                return new MCSLock();
            default:
                throw new IllegalArgumentException("Expected valid lock type value.");
        }
    }
}
