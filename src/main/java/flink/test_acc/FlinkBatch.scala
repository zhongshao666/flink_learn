package flink.test_acc

package flink

import org.apache.flink.api.common.accumulators.IntCounter
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.api.scala._
import org.apache.flink.configuration.Configuration

/**
 * Flink的累加器使用
 */
object FlinkBatch {
  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    val text = env.fromElements("Hello Jason What are you doing Hello world")
    val counts: AggregateDataSet[(String, Int)] = text
      .flatMap(_.toLowerCase.split(" "))
      .map(new RichMapFunction[String, String] {
        //创建累加器
        val acc = new IntCounter()
        override def open(parameters: Configuration): Unit = {
          super.open(parameters)
          //注册累加器
          getRuntimeContext.addAccumulator("accumulator", acc)
        }
        override def map(in: String): String = {
          //使用累加器
          this.acc.add(1)
          println(this.acc.getLocalValue)
          in
        }
      }).setParallelism(4).map((_,1))
      .groupBy(0)
      .sum(1)
    counts.writeAsText("data/test.txt/").setParallelism(1)
//    counts.print()
    val res = env.execute("Accumulator Test")
    //获取累加器的结果
    val num = res.getAccumulatorResult[Int]("accumulator")
    println(num)
  }
}

