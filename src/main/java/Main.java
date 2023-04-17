import org.apache.commons.cli.ParseException;
import utils.ArgsParser;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    private static final int MAX_TASK_NUMBER = 100_000_000;

    public static void main(String[] args) throws ParseException {
        ArgsParser argsParser = new ArgsParser(args);

        int threadsCount = argsParser.getThreadsCount();
        String lockType = argsParser.getLockType().toUpperCase();
        long timeoutMs = argsParser.getTimeoutMs();

        System.out.printf("Benchmark try to start with {\"lockType\": %s, \"nThreads\": %d, \"timeoutMs\": %d}.\n", lockType, threadsCount, timeoutMs);

        ExecutorService service = Executors.newFixedThreadPool(threadsCount);

        Benchmark benchmark = new Benchmark(LockType.valueOf(lockType));
        ArrayList<Benchmark> tasks = new ArrayList<>();
        for (int i = 0; i < MAX_TASK_NUMBER; i++) {
            tasks.add(benchmark);
        }

        LocalDateTime startTime = LocalDateTime.now();
        System.out.println("Start incrementation... in " + startTime);
        try {
            service.invokeAll(tasks, timeoutMs, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            service.shutdown();
            LocalDateTime completeTime = LocalDateTime.now();
            System.out.println("Complete incrementation in \"" + completeTime + "\"");
            try {
                System.out.println("Counter value: " + benchmark.call());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
