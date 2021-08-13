package flink.vlc

import java.time.Duration
import java.util.Properties

import org.apache.flink.api.common.eventtime.{SerializableTimestampAssigner, WatermarkStrategy}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, createTypeInformation}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer



object ViolentLoginCrack {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment =StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration())
    env.setParallelism(1)

    val waterMark: WatermarkStrategy[TestLog] = WatermarkStrategy.forBoundedOutOfOrderness[TestLog](Duration.ofSeconds(10))
      .withTimestampAssigner(new SerializableTimestampAssigner[TestLog] {
        override def extractTimestamp(t: TestLog, l: Long) = t.time
      })

    val properties = new Properties

//    env.addSource(new FlinkKafkaConsumer[]())

  }
}
