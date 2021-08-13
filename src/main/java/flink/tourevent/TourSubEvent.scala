package flink.tourevent

/**
 * 子事件，封装对旅游平台的动作，还有景点容量信息
 */
case class TourSubEvent(id:Int,eventName:String,price:Double,time:Long,volume:Int) extends TourEvent (id,eventName,price,time){


  override def toString: String = s"${super.toString},旅游景点最大容纳量->$volume"
}
