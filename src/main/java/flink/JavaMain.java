package flink;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;

public class JavaMain {
    public static void main(String[] args) throws Exception {
        // 1.获取flink流计算的运行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 2.从collection读取数据
//        DataStreamSource<Integer> nums = env.fromElements(1,2,3,4,5,6,7,8,9);;
        DataStreamSource<Integer> nums = env.fromCollection(Arrays.asList(1,2,3,4,5,6,7,8,9));

        //3.调用Sink
        nums.print();

        //4.启动流计算
        env.execute("FromCollectionReview");

    }
}
