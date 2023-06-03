package warmup.bench;

import org.apache.commons.cli.ParseException;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.infra.Blackhole;
import ru.somepackage.LockTester;
import ru.somepackage.utils.BenchArgs;

import java.util.concurrent.BrokenBarrierException;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
public class MainBenchmark {

    @Param("DEADBEEF")
    private String args;

    @Benchmark
    public void main(Blackhole bh, BenchmarkParams p) throws BrokenBarrierException, ParseException, InterruptedException {
        assert args != "DEADBEEF";
        bh.consume(LockTester.runApp(BenchArgs.of(args).get()));
    }

}
