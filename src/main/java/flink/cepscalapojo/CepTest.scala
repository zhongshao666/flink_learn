package flink.cepscalapojo

import java.time.Duration
import java.util

import lombok.extern.slf4j.Slf4j
import org.apache.flink.api.common.eventtime.{SerializableTimestampAssigner, WatermarkStrategy}
import org.apache.flink.cep.pattern.Pattern
import org.apache.flink.cep.pattern.conditions.{IterativeCondition, SimpleCondition}
import org.apache.flink.cep.{CEP, PatternSelectFunction, PatternStream}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time
import sun.misc.ObjectInputFilter.Config

@Slf4j
//连续登录3次
object CepTest {
  def main(args: Array[String]): Unit = {
//    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration())
    env.setParallelism(1)

    //    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    

    //    list.foreach(l => print(l.getId))

    val value: WatermarkStrategy[LoginEvent1] = WatermarkStrategy
      .forBoundedOutOfOrderness[LoginEvent1](Duration.ofSeconds(5))
      .withTimestampAssigner(new SerializableTimestampAssigner[LoginEvent1] {
        override def extractTimestamp(element: LoginEvent1, recordTimestamp: Long): Long = element.eventTime
      })

    val events: util.List[LoginEvent1] = util.Arrays.asList(
      LoginEvent1(1, "zzh", "fail", 1623392865274L),
      LoginEvent1(1, "zzh", "fail", 1623392865275L),
      LoginEvent1(1, "zzh666", "fail", 1623392865276L),

      LoginEvent1(1, "zzh", "fail",1623392865277L),
      LoginEvent1(1, "zzh", "fail", 1623392865278L),
      LoginEvent1(1, "zzh", "fail", 1623392865279L),
      LoginEvent1(1, "zzh666", "fail", 1623392865280L),
      LoginEvent1(1, "zzh", "fail", 1623392865281L),
      LoginEvent1(1, "zzh", "fail", 1623392865282L),
      LoginEvent1(1, "zzh", "fail", 1623392865283L),
    )
    
    val stream: SingleOutputStreamOperator[LoginEvent1] = env
            .fromCollection(events)
/*      .socketTextStream("192.168.18.240", 8888)
      .filter((_: String).trim.nonEmpty)
      .map(event=>{
        val strings: Array[String] = event.split(",")
        LoginEvent1(strings(0).trim.toInt,strings(1),strings(2),strings(3).trim.toLong)
      })*/
      .assignTimestampsAndWatermarks(new TimeAndMark)

    /*      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[LoginEvent1](Time.seconds(0)) {
      override def extractTimestamp(t: LoginEvent1) = {
        t.getEventTime * 1000
      }
    })*/

    //    val testStream: DataStreamSource[Int] = env.fromCollection(util.Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9))

    stream.print("stream")


    //定义pattern
    val pattern: Pattern[LoginEvent1, LoginEvent1] = Pattern.begin[LoginEvent1]("start1").where(new SimpleCondition[LoginEvent1] {
      override def filter(t: LoginEvent1) = {
        t.eventType == "fail"
      }
    })
      .next("fail2").where(new SimpleCondition[LoginEvent1] {
      override def filter(t: LoginEvent1) = {
        t.eventType == "fail"
      }
    })
      .next("fail3").where(new IterativeCondition[LoginEvent1] {
      override def filter(t: LoginEvent1, context: IterativeCondition.Context[LoginEvent1]) = {
        t.eventType == "fail"
      }
    })
      .within(Time.seconds(10))

//    pattern.timesOrMore(5).greedy()

    // 期望出现4次
//    pattern.times(4);

    // 期望出现0或者4次
//    pattern.times(4).optional();

    // 期望出现2、3或者4次
//    pattern.times(2, 4);

    // 期望出现2、3或者4次，并且尽可能的重复次数多
//    pattern.times(2, 4).greedy();

    // 期望出现0、2、3或者4次
//    pattern.times(2, 4).optional();

    // 期望出现0、2、3或者4次，并且尽可能的重复次数多
//    pattern.times(2, 4).optional().greedy();

    // 期望出现1到多次
//    pattern.oneOrMore();

    // 期望出现1到多次，并且尽可能的重复次数多
//    pattern.oneOrMore().greedy();

    // 期望出现0到多次
//    pattern.oneOrMore().optional();

    // 期望出现0到多次，并且尽可能的重复次数多
//    pattern.oneOrMore().optional().greedy();

    // 期望出现2到多次
//    pattern.timesOrMore(2);

    // 期望出现2到多次，并且尽可能的重复次数多
//    pattern.timesOrMore(2).greedy();

    // 期望出现0、2或多次
//    pattern.timesOrMore(2).optional();

    // 期望出现0、2或多次，并且尽可能的重复次数多
//    pattern.timesOrMore(2).optional().greedy();

    val patternStream: PatternStream[LoginEvent1] = CEP.pattern(stream.keyBy(new Keyed), pattern)

    val result: SingleOutputStreamOperator[String] = patternStream.select(new PatternSelectFunction[LoginEvent1, String] {
      override def select(map: util.Map[String, util.List[LoginEvent1]]) = {
        val keys: util.Iterator[String] = map.keySet().iterator()
        val e1: LoginEvent1 = map.get(keys.next()).iterator().next()
        val e2: LoginEvent1 = map.get(keys.next()).iterator().next()
        val e3: LoginEvent1 = map.get(keys.next()).iterator().next()
        "用户名：" + e1.userName + ",登录时间：" + e1.eventTime + "," + "用户名：" + e2.userName + ",登录时间：" + e2.eventTime + "," + "用户名：" + e3.userName + ",登录时间：" + e3.eventTime
      }
    })

    result.print("cep")

    result.map(t=>{
      Thread.sleep(8000)
    })

    env.execute()


  }
}
