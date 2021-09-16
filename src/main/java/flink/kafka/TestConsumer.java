package flink.kafka;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class TestConsumer {
    public static void main(String[] args) throws IOException {

        System.setProperty("java.security.krb5.conf","/Users/zhongzhihao/IdeaProjects/flink_learn/config/krb5.conf");              //认证代码
        System.setProperty("java.security.auth.login.config","/Users/zhongzhihao/IdeaProjects/flink_learn/config/kafka.jaas");//认证代码
        Properties props = new Properties();
        //集群地址，多个地址用"，"分隔
        props.put("bootstrap.servers", "192.168.18.231:9092");
        props.put("sasl.kerberos.service.name", "kafka");     //认证代码
        props.put("sasl.mechanism", "GSSAPI");                //认证代码
        props.put("security.protocol", "SASL_PLAINTEXT");     //认证代码
        props.put("group.id", "1");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        //创建消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String,String>(props);
        // 订阅topic，可以为多个用,隔开，此处订阅了"test"这个主题
        consumer.subscribe(Arrays.asList("ceptest"));
        //持续监听
        while(true){
            //poll频率
            ConsumerRecords<String,String> consumerRecords = consumer.poll(100);
            for(ConsumerRecord<String,String> consumerRecord : consumerRecords){
                System.out.println("在test中读到：" + consumerRecord.value());
            }
        }
    }
}