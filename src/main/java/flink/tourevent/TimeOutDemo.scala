package flink.tourevent

import java.time.Duration
import java.util
import java.util.{Optional, Properties}

import com.sun.org.apache.xalan.internal.lib.ExsltDatetime.time
import org.apache.flink.api.common.eventtime.{SerializableTimestampAssigner, WatermarkStrategy}
import org.apache.flink.api.common.functions.MapFunction
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.cep.{CEP, PatternSelectFunction, PatternStream, PatternTimeoutFunction}
import org.apache.flink.cep.pattern.Pattern
import org.apache.flink.cep.pattern.conditions.SimpleCondition
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`
import org.apache.flink.streaming.api.scala
import org.apache.flink.util.Collector


//事件超时处理
object TimeOutDemo {
  private val log: Logger = LoggerFactory.getLogger("Main")



  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration())
    env.setParallelism(1)


    //source
    //    env.socketTextStream("192.168.18.240", 8888).print("start")

    val events: util.List[TourEvent] = util.Arrays.asList(
      //      new TourEvent(101, "启动", 0.0, 1623392865277L),
      new TourEvent(101, "登录", 0.0, 1623392865278L),
      new TourEvent(101, "浏览", 12999.0, 1623392865279L),
      TourSubEvent(101, "下单", 12999.0, 1623392865280L, 1000),
      //      new TourEvent(101, "评论", 0.0, 1623392865281L),
      new TourEvent(101, "浏览", 8799, 1623392865282L),
      //      TourSubEvent(101, "下单", 8799, 1623392865283L, 2000),
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


    val properties = new Properties
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.18.240:9092")
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "test666")
    val tourStream: DataStream[TourEvent] = env
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
      .assignTimestampsAndWatermarks(wm)

    tourStream.print("tour")
    tourStream.process(new ProcessFunction[TourEvent,TourEvent] {
      override def processElement(i: TourEvent, context: ProcessFunction[TourEvent, TourEvent]#Context, collector: Collector[TourEvent]): Unit = {
        println(context.timerService().currentWatermark())
      }
    })

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
      .within(Time.seconds(5))

    //1.超时侧输出
    val outputTag: scala.OutputTag[String] =new OutputTag[String]("timeout_data")

    //2.超时事件
/*    val timeoutEvent = (map:Map[String,Iterable[TourEvent],time:Long])=>{
      //获取patternStream中元素
      val loginEvent = map.get("login").toList
      val loginEventStr: String = if(loginEvent.isEmpty) "" else loginEvent.head.toString

      val browseEvent = map.get("browse").toList
      val browseEventStr: String = if(browseEvent.isEmpty) "" else browseEvent.head.toString

      val orderEvent = map.get("order").toList
      val orderEventStr: String = if(orderEvent.isEmpty) "" else orderEvent.head.toString

      val exitEvent= map.get("exit").toList
      val exitEventStr: String = if(exitEvent.isEmpty) "" else exitEvent.head.toString

      //返回
      val result = s"超时的Event,时间戳$time:,$loginEvent\t\t$browseEvent\t\t$orderEvent\t\t$exitEvent"
      result
    }*/

    //3.正常事件
/*
    val normalEvent = (map:Map[String,Iterable[TourEvent],time:Long])=>{
      //获取patternStream中元素
      val loginEvent: String = map.get("login").toList.head.toString

      val browseEvent: String = map.get("browse").toList.head.toString

      val orderEvent: String = map.get("order").toList.head.toString

      val exitEvent: String = map.get("exit").toList.head.toString


      //返回
      val result = s"正常的Event,时间戳:$time,$loginEvent\t\t$browseEvent\t\t$orderEvent\t\t$exitEvent"
      result
    }
*/



    //匹配
    val patternStream: PatternStream[TourEvent] = CEP.pattern(tourStream.keyBy((_: TourEvent).num).javaStream, patternTest)

    //分析结果
    val resultStream: SingleOutputStreamOperator[String] = patternStream.select[String, String](outputTag,
      new PatternTimeoutFunction[TourEvent, String] {
        override def timeout(map: util.Map[String, util.List[TourEvent]], time: Long) = {
          //获取patternStream中元素
          val loginEvent: util.List[TourEvent] = map.get("login")
          val loginEventStr: String = if (loginEvent.isEmpty) "" else loginEvent.head.toString

          val browseEvent: util.List[TourEvent] = map.get("browse")
          var browseEventStr: String= null
          if (Optional.ofNullable(browseEvent).isPresent) {
            browseEventStr = if (browseEvent.isEmpty) "" else browseEvent.head.toString
          }


          val orderEvent: util.List[TourEvent] = map.get("order")
          var orderEventStr: String =null
          if (Optional.ofNullable(orderEvent).isPresent) {
            orderEventStr= if (orderEvent.isEmpty) "" else orderEvent.head.toString
          }


          val exitEvent: util.List[TourEvent] = map.get("exit")
          var exitEventStr: String = null
          if (Optional.ofNullable(exitEvent).isPresent) {
            exitEventStr= if (exitEvent.isEmpty) "" else exitEvent.head.toString
          }

          val result = s"超时的Event,时间戳$time:,$loginEventStr\t\t$browseEventStr\t\t$orderEventStr\t\t$exitEventStr"
          result
        }
      }, new PatternSelectFunction[TourEvent, String] {
        override def select(map: util.Map[String, util.List[TourEvent]]) = {
          //获取patternStream中元素
          val loginEvent: String = map.get("login").toList.head.toString

          val browseEvent: String = map.get("browse").toList.head.toString

          val orderEvent: String = map.get("order").toList.head.toString

          val exitEvent: String = map.get("exit").toList.head.toString


          //返回
          val result = s"正常的Event,时间戳:$time,$loginEvent\t\t$browseEvent\t\t$orderEvent\t\t$exitEvent"
          result
        }
      })


    resultStream.print("result")

    resultStream.getSideOutput(outputTag).print("side")

    //执行
    env.execute(this.getClass.getSimpleName)


  }
}

