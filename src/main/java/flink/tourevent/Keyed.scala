package flink.tourevent

import org.apache.flink.api.java.functions.KeySelector

class Keyed extends KeySelector[TourEvent,Int]{
  override def getKey(in: TourEvent): Int = {
    in.num
  }
}
