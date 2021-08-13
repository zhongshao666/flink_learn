package flink.toureventjava;

import org.apache.flink.api.common.eventtime.*;

public class TimeWaterMark implements WatermarkStrategy<TourEvent> {
    Long maxOutOfOrderne = 5000L; // 5 秒
    Long currentMaxTimestamp = 0L;

    @Override
    public WatermarkGenerator<TourEvent> createWatermarkGenerator(WatermarkGeneratorSupplier.Context context) {
        return new WatermarkGenerator<TourEvent>() {
            @Override
            public void onEvent(TourEvent tourEvent, long l, WatermarkOutput watermarkOutput) {
                currentMaxTimestamp = Math.max(tourEvent.getTime(), currentMaxTimestamp);
            }

            @Override
            public void onPeriodicEmit(WatermarkOutput watermarkOutput) {
                // 发出的 watermark = 当前最大时间戳 - 最大乱序时间
                watermarkOutput.emitWatermark(new Watermark(currentMaxTimestamp - maxOutOfOrderne - 1));
            }
        };
    }

    @Override
    public TimestampAssigner<TourEvent> createTimestampAssigner(TimestampAssignerSupplier.Context context) {
        return new TimestampAssigner<TourEvent>() {
            @Override
            public long extractTimestamp(TourEvent tourEvent, long l) {
                return tourEvent.getTime();
            }
        };
    }
}
