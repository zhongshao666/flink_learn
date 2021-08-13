package flink.curator;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.shaded.curator4.org.apache.curator.framework.api.GetDataBuilder;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.io.ByteArrayInputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class FlinkMain {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        WatermarkStrategy<String> wm = WatermarkStrategy
                .<String>forBoundedOutOfOrderness(Duration.ofMillis(0))
                .withTimestampAssigner(new SerializableTimestampAssigner<String>() {
                    @Override
                    public long extractTimestamp(String s, long l) {
                        return Long.parseLong(s.split(",")[3]);
                    }
                });

        //  --zkAddr 192.168.18.231:2181 --zkPath /zzh666

        System.setProperty("zookeeper.sasl.client", "false");
        ParameterTool paras = ParameterTool.fromArgs(args);
        Properties properties = new Properties();
        CuratorOperator curatorOperator = new CuratorOperator(paras.get("zkAddr"));
        GetDataBuilder data = curatorOperator.client.getData();
        properties.load(new ByteArrayInputStream(data.forPath(paras.get("zkPath"))));

        ParameterTool parameterTool = ParameterTool.fromMap((Map)properties);

        env.getConfig().setGlobalJobParameters(parameterTool);

//        log.info(parameterTool.get("zzh"));

        Properties prop = new Properties();
        prop.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.18.231:9092");
        prop.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "test666");
        SingleOutputStreamOperator<String> stringDataStreamSource = env.addSource(new FlinkKafkaConsumer<String>("ceptest", new SimpleStringSchema(), prop)).assignTimestampsAndWatermarks(wm);

        SingleOutputStreamOperator<String> process = stringDataStreamSource.process(new ProcessFunction<String, String>() {
            @Override
            public void processElement(String s, Context context, Collector<String> collector) throws Exception {
                collector.collect(s);
            }
        });
        SingleOutputStreamOperator<String> process1 = process.process(new TestProcess());
        process1.print();

        env.execute();

    }
}
