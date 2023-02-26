package flink.hour

import java.time.Duration
import org.apache.flink.api.common.eventtime.{SerializableTimestampAssigner, WatermarkStrategy}
import org.apache.flink.streaming.api.functions.{KeyedProcessFunction, ProcessFunction}
import org.apache.flink.streaming.api.functions.windowing.WindowFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.{TumblingEventTimeWindows, TumblingProcessingTimeWindows}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction

import java.{lang, util}

object FlinkHourMain {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment //.createLocalEnvironmentWithWebUI(new Configuration())
    env.setParallelism(1)
    val value: WatermarkStrategy[Event] = WatermarkStrategy
      .forBoundedOutOfOrderness[Event](Duration.ofMinutes(1))
      .withTimestampAssigner(new SerializableTimestampAssigner[Event] {
        override def extractTimestamp(element: Event, recordTimestamp: Long): Long = element.time
      })
    val events: Seq[Event] = Seq(
      Event(0, "x", 1648776323000L),//2022-04-01 09:25:23
      Event(1, "x", 1648776383000L),//2022-04-01 09:26:23
      Event(2, "x", 1648776923006L),//2022-04-01 09:35:23
      Event(3, "x", 1648778483000L),//2022-04-01 10:01:23
      Event(4, "x", 1648780523000L),//2022-04-01 10:35:23
      Event(5, "x", 1648782263000L),//11.01
    )
    val stream: DataStream[Event] = env
      .fromCollection(events).assignTimestampsAndWatermarks(value)

    val keyedStream: KeyedStream[Event, String]  = stream.keyBy(_.name)
    keyedStream.process(new KeyedProcessFunction[String,Event,String] {
      override def processElement(i: Event, context: KeyedProcessFunction[String, Event, String]#Context, collector: Collector[String]): Unit = {
        println("before window time : "+context.timestamp()+" , watermark :"+context.timerService().currentWatermark())
      }
    })
//    keyedStream.print("key: ")
    val res: DataStream[Integer] = keyedStream
      .window(TumblingEventTimeWindows.of(Time.minutes(60)))

      .process(new ProcessWindowFunction[Event, Integer, String, TimeWindow] {
      override def process(key: String, context: Context, elements: Iterable[Event], out: Collector[Integer]): Unit = {

        var sum: Int = 0
        elements.foreach((_: Event) => sum += 1)
        out.collect(sum)
      }
    })

    res.process(new ProcessFunction[Integer,Integer] {
      override def processElement(i: Integer, context: ProcessFunction[Integer, Integer]#Context, collector: Collector[Integer]): Unit = {
        println("after window "+context.timerService().currentWatermark())
      }
    })

    res.print("result: ")

    env.execute()
  }
}
