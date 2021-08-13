package flink.tourevent

/**
 * 旅游事件
 */
class TourEvent (id:Int,eventName:String,price:Double,time:Long){
  /**
   * 旅客身份证号
   */
  val num: Int =id;

  /**
   * 事件，如登录，浏览，下单，评论，退出
   */
  val name: String =eventName

  /**
   *景点价格
   */
  val sellPrice: Double =price

  /**
   * 时间戳
   */
  val timeStamp: Long =time

  override def toString: String = s"旅客id->$num,事件名->$name,景点价格->$sellPrice,时间戳->$timeStamp"
}
