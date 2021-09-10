package flink.cep_demo


import org.apache.flink.api.common.eventtime.{SerializableTimestampAssigner, WatermarkStrategy}
import org.apache.flink.cep.{PatternFlatSelectFunction, PatternSelectFunction}
import org.apache.flink.cep.pattern.conditions.SimpleCondition
import org.apache.flink.cep.scala.{CEP, PatternStream}
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

import java.sql.Timestamp
import java.time.format.DateTimeFormatter
import java.time.{Duration, LocalDateTime, ZoneOffset}
import java.util
import scala.collection.convert.ImplicitConversions.{`collection AsScalaIterable`, `map AsScala`}

object FlinkMain {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment //.createLocalEnvironmentWithWebUI(new Configuration())
    env.setParallelism(1)
    val value: WatermarkStrategy[Event] = WatermarkStrategy
      .forBoundedOutOfOrderness[Event](Duration.ofSeconds(5))
      .withTimestampAssigner(new SerializableTimestampAssigner[Event] {
        override def extractTimestamp(element: Event, recordTimestamp: Long): Long = System.currentTimeMillis()
      })
    val events: Seq[Event] = Seq(
      Event(1, "x", 1623392865000L),
      Event(1, "c", 1623392866000L),
      Event(1, "b", 1623392867000L),
      Event(1, "b", 1623392868000L),
      Event(1, "a", 1623392869000L),
      Event(1, "b", 1623392869000L)
    )
    val stream: DataStream[Event] = env
      .fromCollection(events).assignTimestampsAndWatermarks(value)
    //    stream.print("source")
    val value1: DataStream[Event] = new DataStream[Event](stream.javaStream) // scala->java   java->scala


    val pattern: Pattern[Event, Event] = Pattern.begin[Event]("start").where((_: Event).name.equals("c")) //.optional
      .followedByAny("second").where((_: Event).name.equals("a")).optional

    val patternStream: PatternStream[Event] = CEP.pattern(stream, pattern)


    val resultStream: DataStream[String] = patternStream.select(new SelectFunc)
    val resultStream2: DataStream[String] = patternStream.select((map: collection.Map[String, Iterable[Event]]) => {
      deal(map)
    })

    val outputTag: OutputTag[String] = OutputTag[String]("side-output")
    val resultStream3: DataStream[String] = patternStream.flatSelect(new FlatSelectFunc)

    val outStream: DataStream[String] = resultStream3.getSideOutput(outputTag)
    resultStream3.print("result3")
    outStream.print("sideOutput")


    resultStream.print("result")
    resultStream2.print("result2")
    env.execute()


    //    val now: LocalDateTime = LocalDateTime.now
    //    val milli: Long = now.toInstant(ZoneOffset.of("+8")).toEpochMilli
    //    val l: Long = Timestamp.valueOf(LocalDateTime.now()).getTime
    //    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
    //    val str: String = now.format(formatter)
  }
  def deal(map: collection.Map[String, Iterable[Event]]): String ={
    val e1: String =
      if (map.contains("start")) {
        map.get("start").toList.head.toString
      } else ""
    val e2: String = if (map.contains("second")) {
      map.get("second").toList.head.toString
    } else ""
    e1 + " " + e2
  }
}

class SelectFunc extends PatternSelectFunction[Event, String] {
  override def select(map: util.Map[String, util.List[Event]]): String = {
    val e1: String =
      if (map.contains("start")) {
        map.get("start").toList.head.toString
      } else ""
    val e2: String =
      if (map.contains("second")) {
        map.get("second").toList.head.toString
      } else ""
    e1 + " " + e2
  }
}

class FlatSelectFunc extends PatternFlatSelectFunction[Event,String]{
  override def flatSelect(map: util.Map[String, util.List[Event]], collector: Collector[String]): Unit = {
    val e1: String =
      if (map.contains("start")) {
        map.get("start").toList.head.toString
      } else ""

    val e2: String =
      if (map.contains("second")) {
        map.get("second").toList.head.toString
      } else ""
    collector.collect(e1 + " " + e2)
  }
}

//class FlatSelectFunc2 extends