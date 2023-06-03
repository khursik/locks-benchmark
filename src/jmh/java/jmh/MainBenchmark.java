package jmh;

import org.apache.commons.cli.ParseException;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.BrokenBarrierException;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
public class MainBenchmark {

    @org.openjdk.jmh.annotations.Benchmark
    public void main(Blackhole bh) throws BrokenBarrierException, ParseException, InterruptedException {
        String[] args =  new String[]{"--type", "TAS", "--threadsCount", "20", "--timeoutMs", "10000"};
        bh.consume(ru.jmh.Benchmark.runApp(args));
    }

}
