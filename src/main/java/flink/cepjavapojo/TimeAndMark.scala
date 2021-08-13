package flink.cepjavapojo

import org.apache.flink.api.common.eventtime._


class TimeAndMark extends WatermarkStrategy[LoginEvent]{

  val maxOutOfOrderne = 3500L // 3.5 秒
  var currentMaxTimestamp: Long = 0
  override def createWatermarkGenerator(context: WatermarkGeneratorSupplier.Context): WatermarkGenerator[LoginEvent] = {
    new WatermarkGenerator[LoginEvent] {
      override def onEvent(t: LoginEvent, l: Long, watermarkOutput: WatermarkOutput): Unit = {
        t.getEventTime

        currentMaxTimestamp = Math.max(t.getEventTime, currentMaxTimestamp)
      }

      override def onPeriodicEmit(watermarkOutput: WatermarkOutput): Unit = {
        // 发出的 watermark = 当前最大时间戳 - 最大乱序时间
        watermarkOutput.emitWatermark(new Watermark(currentMaxTimestamp - maxOutOfOrderne - 1))
      }
    }
  }


  override def createTimestampAssigner(context: TimestampAssignerSupplier.Context): TimestampAssigner[LoginEvent] = {
    new TimestampAssigner[LoginEvent] {
      override def extractTimestamp(t: LoginEvent, l: Long) = {
        t.getEventTime
      }
    }
  }
}

/**
val maxOutOfOrderne = 3500L // 3.5 秒
  var currentMaxTimestamp: Long = 0
  override def createWatermarkGenerator(context: WatermarkGeneratorSupplier.Context): WatermarkGenerator[LoginEvent] = {
    new WatermarkGenerator[LoginEvent] {
      override def onEvent(t: LoginEvent, l: Long, watermarkOutput: WatermarkOutput): Unit = {
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
