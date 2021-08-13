package flink.cepscalapojo

import org.apache.flink.api.common.eventtime._


class TimeAndMark extends WatermarkStrategy[LoginEvent1]{

  val maxOutOfOrderne = 5000L // 5 秒
  var currentMaxTimestamp: Long = 0
  override def createWatermarkGenerator(context: WatermarkGeneratorSupplier.Context): WatermarkGenerator[LoginEvent1] = {
    new WatermarkGenerator[LoginEvent1] {
      override def onEvent(t: LoginEvent1, l: Long, watermarkOutput: WatermarkOutput): Unit = {

        currentMaxTimestamp = Math.max(t.eventTime, currentMaxTimestamp)
      }

      override def onPeriodicEmit(watermarkOutput: WatermarkOutput): Unit = {
        // 发出的 watermark = 当前最大时间戳 - 最大乱序时间
        watermarkOutput.emitWatermark(new Watermark(currentMaxTimestamp - maxOutOfOrderne - 1))
      }
    }
  }


  override def createTimestampAssigner(context: TimestampAssignerSupplier.Context): TimestampAssigner[LoginEvent1] = {
    new TimestampAssigner[LoginEvent1] {
      override def extractTimestamp(t: LoginEvent1, l: Long) = {
        t.eventTime
      }
    }
  }
}

/**
val maxOutOfOrderne = 3500L // 3.5 秒
  var currentMaxTimestamp: Long = 0
  override def createWatermarkGenerator(context: WatermarkGeneratorSupplier.Context): WatermarkGenerator[LoginEvent1] = {
    new WatermarkGenerator[LoginEvent1] {
      override def onEvent(t: LoginEvent1, l: Long, watermarkOutput: WatermarkOutput): Unit = {
        t.getEventTime

        currentMaxTimestamp = Math.max(t.getEventTime, currentMaxTimestamp)
      }

      override def onPeriodicEmit(watermarkOutput: WatermarkOutput): Unit = {
        // 发出的 watermark = 当前最大时间戳 - 最大乱序时间
        watermarkOutput.emitWatermark(new Watermark(currentMaxTimestamp - maxOutOfOrderne - 1))
      }
    }
  }
*/
