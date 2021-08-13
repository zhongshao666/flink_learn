package flink.cepjavapojo

import java.time.Duration
import java.util

import lombok.extern.slf4j.Slf4j
import org.apache.flink.api.common.eventtime.{SerializableTimestampAssigner, WatermarkStrategy}
import org.apache.flink.cep.pattern.Pattern
import org.apache.flink.cep.pattern.conditions.SimpleCondition
import org.apache.flink.cep.{CEP, PatternSelectFunction, PatternStream}
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time

@Slf4j
//连续登录3次
object CepTest {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    //    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val l = List(1, 2, 3, 5)
    new LoginEvent(1, "zzh", "fail", 1592080457)
    val list = List(1, 2, 3)

    //    list.foreach(l => print(l.getId))

    val value: WatermarkStrategy[LoginEvent] = WatermarkStrategy
      .forBoundedOutOfOrderness[LoginEvent](Duration.ofSeconds(5))
      .withTimestampAssigner(new SerializableTimestampAssigner[LoginEvent] {
        override def extractTimestamp(element: LoginEvent, recordTimestamp: Long): Long = element.getEventTime
      })

    val events: util.List[LoginEvent1] = util.Arrays.asList(
      LoginEvent1(1, "zzh", "fail", System.currentTimeMillis()),
      LoginEvent1(1, "zzh", "fail", System.currentTimeMillis()),
      LoginEvent1(1, "zzh", "fail", System.currentTimeMillis()),
      LoginEvent1(1, "zzh", "fail", System.currentTimeMillis())
    )

    val events1: util.List[LoginEvent] = util.Arrays.asList(
      new LoginEvent(1, "zzh", "fail", System.currentTimeMillis()),
      new LoginEvent(1, "zzh", "fail", System.currentTimeMillis()),
      new LoginEvent(1, "zzh", "fail", System.currentTimeMillis()),
      new LoginEvent(1, "zzh", "fail", System.currentTimeMillis()))
    val stream: SingleOutputStreamOperator[LoginEvent] = env.fromCollection(events1)
      .assignTimestampsAndWatermarks(new TimeAndMark)


    /*      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[LoginEvent](Time.seconds(0)) {
      override def extractTimestamp(t: LoginEvent) = {
        t.getEventTime * 1000
      }
    })*/

    //    val testStream: DataStreamSource[Int] = env.fromCollection(util.Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9))

    stream.print("stream")


    //定义pattern
    val pattern: Pattern[LoginEvent, LoginEvent] = Pattern.begin[LoginEvent]("start").where(new SimpleCondition[LoginEvent] {
      override def filter(t: LoginEvent) = {
        t.getEventType == "fail"
      }
    })
      .next("fail2").where(new SimpleCondition[LoginEvent] {
      override def filter(t: LoginEvent) = {
        t.getEventType == "fail"
      }
    })
      .next("fail3").where(new SimpleCondition[LoginEvent] {
      override def filter(t: LoginEvent) = {
        t.getEventType == "fail"
      }
    })
      .within(Time.seconds(10))

    val patternStream: PatternStream[LoginEvent] = CEP.pattern(stream.keyBy(new Keyed), pattern)

    val result: SingleOutputStreamOperator[String] = patternStream.select(new PatternSelectFunction[LoginEvent, String] {
      override def select(map: util.Map[String, util.List[LoginEvent]]) = {
        val keys: util.Iterator[String] = map.keySet().iterator()
        val e1: LoginEvent = map.get(keys.next()).iterator().next()
        val e2: LoginEvent = map.get(keys.next()).iterator().next()
        val e3: LoginEvent = map.get(keys.next()).iterator().next()
        "用户名：" + e1.getUserName + ",登录时间：" + e1.getEventTime + "," + "用户名：" + e2.getUserName + ",登录时间：" + e2.getEventTime + "," + "用户名：" + e3.getUserName + ",登录时间：" + e3.getEventTime
      }
    })

    result.print("cep")


    env.execute()


  }
}
