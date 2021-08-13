package flink.cepjavapojo

import org.apache.flink.api.java.functions.KeySelector

class Keyed extends KeySelector[LoginEvent,String]{
  override def getKey(in: LoginEvent): String = {
    in.getUserName
  }
}
