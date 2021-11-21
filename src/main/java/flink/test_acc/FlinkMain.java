package flink.test_acc;



import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.accumulators.Accumulator;
import org.apache.flink.api.common.functions.RichFilterFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//public class FlinkMain {
//
//    private static final String EMPTY_FIELD_ACCUMULATOR = "empty-fields";
//
//    public static void main(final String[] args) throws Exception {
//
//        final ParameterTool params = ParameterTool.fromArgs(args);
//
//        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
//        env.getConfig().setGlobalJobParameters(params);
//
//        // 1. 得到数据集
//        final DataSet<StringTriple> file = getDataSet(env, params);
//
//        // 2. 过滤含有空值的行
//        final DataSet<StringTriple> filteredLines = file.filter(new EmptyFieldFilter());
//
//        JobExecutionResult result;
//        // 3. 执行任务并输出过滤行
//        if (params.has("output")) {
//            filteredLines.writeAsCsv(params.get("output"));
//            // 执行程序
//            result = env.execute("Accumulator example");
//        } else {
//            System.out.println("Printing result to stdout. Use --output to specify output path.");
//            filteredLines.print();
//            result = env.getLastJobExecutionResult();
//        }
//
//        // 4. 通过注册时的key值来获得累加器的结果
//        final List<Integer> emptyFields = result.getAccumulatorResult(EMPTY_FIELD_ACCUMULATOR);
//        System.out.format("Number of detected empty fields per column: %s\n", emptyFields);
//    }
//}
