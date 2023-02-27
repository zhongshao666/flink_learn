package flink.checkpoint;

import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class FlinkMain {
    public static void main(String[] args){
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());
        // 每隔 1 秒启动一次检查点保存
        env.enableCheckpointing(60*1000);

        env.setStateBackend(new FsStateBackend("hdfs://1.1.1.1/flink/cp/my_job", true));
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(10, Time.seconds(10)));

        CheckpointConfig checkpointConfig = env.getCheckpointConfig();
        checkpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        // 设置精确一次模式
        checkpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        // 最小间隔时间 500 毫秒
        checkpointConfig.setMinPauseBetweenCheckpoints(500);
        // 超时时间 1 分钟
        checkpointConfig.setCheckpointTimeout(60000);
        // 同时只能有一个检查点
        checkpointConfig.setMaxConcurrentCheckpoints(1);
        // 开启检查点的外部持久化保存，作业取消后依然保留
        checkpointConfig.enableExternalizedCheckpoints( CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        // 启用不对齐的检查点保存方式
        checkpointConfig.enableUnalignedCheckpoints();

        // 配置存储检查点到 JobManager 堆内存
        // 配置存储检查点到文件系统


    }
}
