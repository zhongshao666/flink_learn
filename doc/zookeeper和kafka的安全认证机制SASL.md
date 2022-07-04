#### zookeeper Kafka jaas 集群

##### zookeeper集群

zoo.cfg

```
admin.serverPort=8888
tickTime=2000
initLimit=10
syncLimit=5
dataDir=/data/zookeeperData/
clientPort=2181
server.1=0.0.0.0:2888:3888
server.2=node2:2888:3888
server.3=node3:2888:3888

#server点几，那台机器就是0.0.0.0
```

```
每台机器1 2 3...
mkdir -p /data/zookeeperData/
echo 1 > myid
```

##### kafka集群

server.properties   

##### 注意每台broker.id不一样

```
broker.id=1
listeners=PLAINTEXT://0.0.0.0:9092
advertised.listeners=PLAINTEXT://192.168.18.241:9092
log.dirs=/data/kafkaData
zookeeper.connect=node1:2181,node2:2181,node3:2181
```



## zookeeper和kafka的安全认证机制SASL

zookeeper在生产环境中，如果不是只在内网开放的话，就需要设置安全认证，可以选择SASL的安全认证。以下是和kafka的联合配置，如果不需要kafka可以去掉kafka相关的权限即可，以下基于zk3.6.2和kafka2.7进行操作。
下面就是详细的部署步骤：

##### zookeeper的安全认证配置
zookeeper所有节点都是对等的，只是各个节点角色可能不相同。以下步骤所有的节点配置相同。

1、导入kafka的相关jar
从kafka/lib目录下复制以下几个jar包到zookeeper的lib目录下：

```
kafka-clients-2.3.0.jar
lz4-java-1.6.0.jar
slf4j-api-1.7.25.jar
slf4j-log4j12-1.7.25.jar
snappy-java-1.1.7.3.jar
```

2、zoo.cfg文件配置
添加如下配置：

```
authProvider.1=org.apache.zookeeper.server.auth.SASLAuthenticationProvider
requireClientAuthScheme=sasl
jaasLoginRenew=3600000
```

3、编写JAAS文件，zk_server_jaas.conf，放置在conf目录下
这个文件定义需要链接到Zookeeper服务器的用户名和密码。JAAS配置节默认为Server：

```
Server {
org.apache.kafka.common.security.plain.PlainLoginModule required 
    username="admin" 
    password="admin-2019" 
    user_kafka="kafka-2019" 
    user_producer="prod-2019";
};
```

这个文件中定义了两个用户，一个是kafka，一个是producer，这些用user_配置出来的用户都可以提供给生产者程序和消费者程序认证使用。还有两个属性，username和password，其中username是配置Zookeeper节点之间内部认证的用户名，password是对应的密码。
4、[修改zkEnv.sh](http://xn--zkenv-3u3h158j.sh/)
在zkEnv.sh添加以下内容，路径按你直接的实际路径来填写：

```
export SERVER_JVMFLAGS=" -Djava.security.auth.login.config=/opt/apache-zookeeper-3.6.2-bin/conf/zk_server_jaas.conf "
```

5、在各个节点分别执行bin/zkServer.sh start启动zk。如果启动异常查看日志排查问题。

### 7.2 kafka的安全认证配置

zookeeper启动之后，就配置kafka,下面步骤的配置在所有节点上都相同。

1、在kafka的config目录下，新建kafka_server_jaas.conf文件，内容如下：

```
KafkaServer {
	org.apache.kafka.common.security.plain.PlainLoginModule required
	username="admin"
	password="admin-2019"
	user_admin="admin-2019"
	user_producer="prod-2019"
	user_consumer="cons-2019";
};

KafkaClient {
        org.apache.kafka.common.security.plain.PlainLoginModule required
        username="kafka"
        password="kafka-2019";
};

Client {
org.apache.kafka.common.security.plain.PlainLoginModule required
	username="kafka"
	password="kafka-2019";
};
```

KafkaServer配置的是kafka的账号和密码，Client配置节主要配置了broker到Zookeeper的链接用户名密码，这里要和前面zookeeper配置中的zk_server_jaas.conf中user_kafka的账号和密码相同。

##### admin/admin-2019  是java代码连接的账号

2、配置server.properties，同样的在config目录下

```
listeners=SASL_PLAINTEXT://0.0.0.0:9092
advertised.listeners=SASL_PLAINTEXT://192.168.244.71:9092
security.inter.broker.protocol=SASL_PLAINTEXT  
sasl.enabled.mechanisms=PLAIN  
sasl.mechanism.inter.broker.protocol=PLAIN  
authorizer.class.name=kafka.security.auth.SimpleAclAuthorizer
allow.everyone.if.no.acl.found=true
```

注意，allow.everyone.if.no.acl.found这个配置项默认是false，若不配置成true，后续生产者、消费者无法正常使用Kafka。

3、在server启动脚本JVM参数,在bin目录下的kafka-server-start.sh中，将

```
export KAFKA_HEAP_OPTS="-Xmx1G -Xms1G"
```

修改为

```
export KAFKA_HEAP_OPTS="-Xmx1G -Xms1G -Djava.security.auth.login.config=/opt/kafka_2.12-2.7.0/config/kafka_server_jaas.conf"
```

4、配置其他节点
配置剩余的kafka broker节点，注意server.properties的listeners配置项



#### java连接kafka

```
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.HashMap;
import java.util.Properties;

public class JaasKafkaProducer {
    private static Properties kafkaProps;

    private static void initKafka(String[] args) {
        kafkaProps = new Properties();
        // broker url
        //在默认kafka的单节点配置时，不能使用IP，而是使用localhost进行连接，否则会连接异常。
        //用于初始化时建立链接到kafka集群，以host:port形式，多个以逗号分隔host1:port1,host2:port2；
        kafkaProps.put("bootstrap.servers","192.168.244.71:9092"); //,192.168.216.139:9092,192.168.216.140:9092
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
        kafkaProps.setProperty("security.protocol", "SASL_PLAINTEXT");
        kafkaProps.setProperty("sasl.mechanism", "PLAIN");
        kafkaProps.setProperty("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username='admin' password='admin-2019';");
        kafkaProps.setProperty("sasl.kerberos.service.name", "kafka");
    }
    public static void main(String[] args) throws InterruptedException {
        new HashMap<>();
        initKafka(args );
        Producer<String, String> producer = new KafkaProducer<>(kafkaProps);

        for (int i = 0; i <20 ; i++) {
            producer.send(new ProducerRecord<>("test666", "66666666666"));
        }

        System.out.println("Message sent successfully!");
        producer.close();
    }
}

```

#### flink消费

````
import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.datastream.DataStreamSource
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer}
import org.apache.kafka.clients.consumer.ConsumerConfig


object KafkaTest {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val properties = new Properties()
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.244.71:9092")
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "test")
    properties.setProperty("security.protocol", "SASL_PLAINTEXT");
    properties.setProperty("sasl.mechanism", "PLAIN");
    properties.setProperty("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username='admin' password='admin-2019';");
    properties.setProperty("sasl.kerberos.service.name", "kafka");
    val kafkaStream: DataStreamSource[String] = env.addSource(new FlinkKafkaConsumer[String]("test666", new SimpleStringSchema(), properties))
    kafkaStream.print("test666")

    env.execute()
  }
}
````


#### flume消费
增加kafka的jaas
修改flume-ng,增加OPTS
flume的conf中打开SASL_PLAINTEXT