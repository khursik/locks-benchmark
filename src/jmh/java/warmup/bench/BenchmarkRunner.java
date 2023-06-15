package warmup.bench;

import org.apache.commons.cli.ParseException;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.infra.Blackhole;
import ru.benchmark.Benchmark;
import ru.benchmark.utils.BenchArgs;

import java.util.Objects;
import java.util.concurrent.BrokenBarrierException;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 6)
@Measurement(iterations = 6)
public class BenchmarkRunner {

    @Param("DEADBEEF")
    private String args;

    @org.openjdk.jmh.annotations.Benchmark
    public void main(Blackhole bh) throws BrokenBarrierException, ParseException, InterruptedException {
        assert !Objects.equals(args, "DEADBEEF");
        bh.consume(Benchmark.benchmark(BenchArgs.of(args).getArgs()));
    }

}
