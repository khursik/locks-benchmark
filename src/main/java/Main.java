import org.apache.commons.cli.ParseException;
import utils.ArgsParser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws ParseException {
        ArgsParser argsParser = new ArgsParser(args);

        int threadsCount = argsParser.getThreadsCount();
        String lockType = argsParser.getLockType().toUpperCase();
        long timeoutMs = argsParser.getTimeoutMs();
        System.out.printf("Benchmark try to start  with \"lockType\": %s, \"nThreads\": %d, \"timeoutMs\": %d}.", lockType, threadsCount, timeoutMs);

        ExecutorService service = new ThreadPoolExecutor(threadsCount, threadsCount, timeoutMs, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        Benchmark benchmark = new Benchmark(LockType.valueOf(lockType));

        LocalDateTime startTime = LocalDateTime.now();
        System.out.println("Start incrementation... in " + startTime);
        try {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                service.submit(benchmark);
            }
        } finally {
            service.shutdown();
            System.out.println("Complete incrementation in \"" + LocalDateTime.now() + "\"");
            System.out.println("Counter value: ");
        }

    }
}
