package flink.curator;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

@Slf4j
public class TestProcess extends ProcessFunction<String,String> {
    ParameterTool parameterTool;

    @Override
    public void open(Configuration parameters) throws Exception {
        parameterTool = (ParameterTool) getRuntimeContext().getExecutionConfig().getGlobalJobParameters();
    }

    @Override
    public void processElement(String s, Context context, Collector<String> collector) throws Exception {
//        log.info(parameterTool.get("zzh"));
        System.out.println(parameterTool.get("zzh"));
        System.out.println(context.timerService().currentWatermark());
        collector.collect(s);
    }
}
