package flink;

import lombok.val;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Arrays;
import java.util.Properties;

public class JavaMain {
    public static void main(String[] args) throws Exception {
        // 1.获取flink流计算的运行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 2.从collection读取数据
//        DataStreamSource<Integer> nums = env.fromElements(1,2,3,4,5,6,7,8,9);;
        DataStreamSource<Integer> nums = env.fromCollection(Arrays.asList(1,2,3,4,5,6,7,8,9));

        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.244.71:9092");
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "test");
        properties.setProperty("security.protocol", "SASL_PLAINTEXT");
        properties.setProperty("sasl.mechanism", "PLAIN");
        properties.setProperty("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username='admin' password='admin-2019';");
        properties.setProperty("sasl.kerberos.service.name", "kafka");
        new FlinkKafkaConsumer<String>("test666", new SimpleStringSchema(), properties);

        //3.调用Sink
        nums.print();

        //4.启动流计算
        env.execute("FromCollectionReview");

    }
}
