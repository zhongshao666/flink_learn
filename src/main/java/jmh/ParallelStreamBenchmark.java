package jmh;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)       //←---- 测量用于执行基准测试目标方法所花费的平均时间
@OutputTimeUnit(TimeUnit.MILLISECONDS)       //←---- 以毫秒为单位，打印输出基准测试的结果
@Fork(jvmArgs={"-Xms4G", "-Xmx4G"})       //←---- 采用4Gb的堆，执行基准测试两次以获得更可靠的结果
public class ParallelStreamBenchmark {
    private static final long N= 10_000_000L;

    @Benchmark       //←---- 基准测试的目标方法
    public long sequentialSum() {
        return Stream.iterate(1L, i -> i + 1).limit(N)
                .reduce( 0L, Long::sum);
    }
    @TearDown(Level.Invocation)       //←---- 尽量在每次基准测试迭代结束后都进行一次垃圾回收
    public void tearDown() {
        System.gc();
    }

}