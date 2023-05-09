import locks.*;
import org.apache.commons.cli.ParseException;
import utils.ArgsParser;
import utils.LockType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Benchmark {
    private static volatile long counter = 0;

    public static void main(String[] args) throws ParseException, InterruptedException, BrokenBarrierException {
        long startTime = System.currentTimeMillis();
        System.out.println("Benchmark started in " + startTime);

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

                while(gate.getNumberWaiting() < 1) {
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
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }

        Thread.sleep(timeoutMs);
        gate.await();

        long completeTime = System.currentTimeMillis();
        System.out.println("Complete incrementation in " + completeTime);
        long totalTime = completeTime - startTime;
        System.out.printf("Total time: %s ms\n", totalTime);
        System.out.println("Counter value: " + counter);
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
