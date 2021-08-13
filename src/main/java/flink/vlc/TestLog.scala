package flink.vlc

case class TestLog(id:Int,userName:String,time:Long){
  override def toString: String = {
    s"id->$id,username->$userName,time->$time "
  }
}
