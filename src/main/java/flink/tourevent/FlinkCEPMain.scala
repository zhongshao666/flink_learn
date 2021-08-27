package flink.tourevent


import java.time.Duration
import java.util
import java.util.Properties

import lombok.extern.slf4j.Slf4j
import org.apache.flink.api.common.eventtime.{SerializableTimestampAssigner, WatermarkStrategy}
import org.apache.flink.api.common.functions.MapFunction
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.cep.{CEP, PatternSelectFunction, PatternStream}
import org.apache.flink.cep.pattern.Pattern
import org.apache.flink.cep.pattern.conditions.{IterativeCondition, SimpleCondition}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`

//@Slf4j
object FlinkCEPMain {
  private val log: Logger = LoggerFactory.getLogger("Main")

  def main(args: Array[String]): Unit = {
    //    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //    env.setParallelism(1)
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration())
    env.setParallelism(1)

    //    env.setStreamTimeCharacteristic()


    //source
    //    env.socketTextStream("192.168.18.240", 8888).print("start")

    val events: util.List[TourEvent] = util.Arrays.asList(
      //      new TourEvent(101, "启动", 0.0, 1623392865277L),
      new TourEvent(101, "登录", 0.0, 1623392865278L),
      new TourEvent(101, "浏览", 12999.0, 1623392865279L),
      TourSubEvent(101, "下单", 12999.0, 1623392865280L, 1000),
      //      new TourEvent(101, "评论", 0.0, 1623392865281L),
      new TourEvent(101, "浏览", 8799, 1623392865282L),
      new TourEvent(101, "退出", 0.0, 1623392865284L)
    )

    val wm: WatermarkStrategy[TourEvent] = WatermarkStrategy
      .forBoundedOutOfOrderness[TourEvent](Duration.ofSeconds(5))
      .withTimestampAssigner(new SerializableTimestampAssigner[TourEvent] {
        override def extractTimestamp(element: TourEvent, recordTimestamp: Long): Long = {
          //         System.currentTimeMillis()
          element.timeStamp
        }
      })


    val properties = new Properties()
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.18.231:9092")
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "test666")
    val tourStream: SingleOutputStreamOperator[TourEvent] = env
      .addSource(new FlinkKafkaConsumer[String]("ceptest", new SimpleStringSchema(), properties))
      .filter((_: String).trim.nonEmpty)
      .map(new MapFunction[String, TourEvent] {
        override def map(t: String) = {
          //101,启动,0.0     102,登录,0.0
          val arr: Array[String] = t.split(",")
          val id: Int = arr(0).trim.toInt
          val name: String = arr(1).trim
          val price: Double = arr(2).trim.toDouble
          val time: Long = arr(3).trim.toLong
          var volume: Int = 0
          if (arr.length == 5) {
            volume = arr(4).trim.toInt
            TourSubEvent(id, name, price, time, volume)
          }
          else
            new TourEvent(id, name, price, time)
        }
      })

      //            .fromCollection(events)

      /*      .socketTextStream("192.168.18.240", 8888)
            .filter((_: String).trim.nonEmpty)
            .map(eventInfo => {
              //101,启动,0.0     102,登录,0.0
              val arr: Array[String] = eventInfo.split(",")
              val id: Int = arr(0).trim.toInt
              val name: String = arr(1).trim
              val price: Double = arr(2).trim.toDouble
              val time: Long = arr(3).trim.toLong
              var volume: Int = 0
              if (arr.length == 5) {
                volume = arr(4).trim.toInt
                TourSubEvent(id, name, price, time,volume)
              }
              else
                new TourEvent(id, name, price,time)
            })*/
      .assignTimestampsAndWatermarks(wm)

    tourStream.print("tour")

    //CEP Pattern  登录->浏览->下单->退出  筛选出来
    val patternTest: Pattern[TourEvent, TourEvent] = Pattern.begin[TourEvent]("login")
      .where(new SimpleCondition[TourEvent] {
        override def filter(t: TourEvent) =
          t.name.equals("登录")
      })
      .followedByAny("browse")
      .where(new SimpleCondition[TourEvent] {
        override def filter(t: TourEvent) =
          t.name.equals("浏览")
      }).oneOrMore()
      .followedByAny("order")
      .where(new SimpleCondition[TourEvent] {
        override def filter(t: TourEvent) =
          t.name.equals("下单")
      })
      .followedByAny("exit")
      .where(new SimpleCondition[TourEvent] {
        override def filter(t: TourEvent) =
          t.name.equals("退出")
      })
      .within(Time.seconds(20))


    //匹配
    val patternStream: PatternStream[TourEvent] = CEP.pattern(tourStream.keyBy(new Keyed), patternTest)

    //分析结果
    val outStream: SingleOutputStreamOperator[String] = patternStream.select(new PatternSelectFunction[TourEvent, String] {
      override def select(map: util.Map[String, util.List[TourEvent]]) = {
        val loginEvent: String = map.get("login").toList.head.toString
        val browseEvent: String = map.get("browse").toList.head.toString
        val orderEvent: String = map.get("order").toList.head.toString
        val exitEvent: String = map.get("exit").toList.head.toString

        //返回
        val result = s"$loginEvent\t\t$browseEvent\t\t$orderEvent\t\t$exitEvent"
        result
        /*        log.info("successful")
                "successful"*/
      }
    })

    outStream.print("result")


    //执行
    env.execute(this.getClass.getSimpleName)


  }

}
