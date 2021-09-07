package flink

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
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "test666")
    val kafkaStream: DataStreamSource[String] = env.addSource(new FlinkKafkaConsumer[String]("test666", new SimpleStringSchema(), properties))
    kafkaStream.print("test666")

/*    val partternTopic = new FlinkKafkaConsumer[String](
      java.util.regex.Pattern.compile("test-topic-[0-9]"),
      new SimpleStringSchema(),
      properties
    )
    val kafkaPartternStream = env.addSource(partternTopic)
    kafkaPartternStream.print()*/

    val propertiesPro = new Properties
    propertiesPro.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    val kafkaPro = new FlinkKafkaProducer[String](
      "test777",
      new SimpleStringSchema(),
      properties
    )


    kafkaStream.addSink(kafkaPro)
    env.execute()
  }
}
