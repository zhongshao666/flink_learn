package flink.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.ArrayList;
import java.util.Properties;

/**
 * kafka生产者<Br>
 */
public class TestProducerOld {

    private static Properties kafkaProps;

    private static void initKafka() {
        kafkaProps = new Properties();
        // broker url
        //在默认kafka的单节点配置时，不能使用IP，而是使用localhost进行连接，否则会连接异常。
        //用于初始化时建立链接到kafka集群，以host:port形式，多个以逗号分隔host1:port1,host2:port2；
        kafkaProps.put("bootstrap.servers", "localhost:9092"); //,192.168.216.139:9092,192.168.216.140:9092
        // 请求需要验证
        //生产者需要server端在接收到消息后，进行反馈确认的尺度，主要用于消息的可靠性传输；acks=0表示生产者不需要来自server的确认；acks=1表示server端将消息保存后即可发送ack，而不必等到其他follower角色的都收到了该消息；acks=all(or acks=-1)意味着server端将等待所有的副本都被接收后才发送确认。
        kafkaProps.put("acks", "all");
        // 请求失败的尝试次数
        //:生产者发送失败后，重试的次数 batch.size:当多条消息发送到同一个partition时
        kafkaProps.put("retries", 0);
        // 缓存大小
        kafkaProps.put("batch.size", 16384);
        //:默认情况下缓冲区的消息会被立即发送到服务端，即使缓冲区的空间并没有被用完。可以将该值设置为大于0的值，这样发送者将等待一段时间后，再向服务端发送请求，以实现每次请求可以尽可能多的发送批量消息。
        kafkaProps.put("linger.ms", 1);
        //生产者缓冲区的大小，保存的是还未来得及发送到server端的消息，如果生产者的发送速度大于消息被提交到server端的速度，该缓冲区将被耗尽。
        kafkaProps.put("buffer.memory", 33554432);
        //定义的key和value序列化器
        //说明了使用何种序列化方式将用户提供的key和vaule值序列化成
        kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    }

    public static void main(String[] args) throws InterruptedException {
        initKafka();
        Producer<String, String> producer = new KafkaProducer<>(kafkaProps);

        ArrayList<String> arr = new ArrayList<>();
        arr.add("119,登录,0.0," + System.currentTimeMillis());
        Thread.sleep(119);
        arr.add("119,浏览,12999," + System.currentTimeMillis());
        Thread.sleep(119);
        arr.add("119,下单,12999," + System.currentTimeMillis() + ",8000");
        Thread.sleep(119);
        arr.add("119,退出,0.0," + System.currentTimeMillis());

//        arr.add("119,登录,0.0,1623895339250");
//        arr.add("119,浏览,12999,1623895339251");
//        arr.add("119,下单,12999,1623895339252,1190");
//        arr.add("119,退出,0.0,1623895339253");


        arr.forEach(a -> {
            producer.send(new ProducerRecord<>("test666", a));
        });


        System.out.println("Message sent successfully!");
        producer.close();
    }
}