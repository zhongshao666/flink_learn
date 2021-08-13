package flink.cepscalapojo

import org.apache.flink.api.java.functions.KeySelector

class Keyed extends KeySelector[LoginEvent1,String]{
  override def getKey(in: LoginEvent1): String = {
    in.userName
  }
}
