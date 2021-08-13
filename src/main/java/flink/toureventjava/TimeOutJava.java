package flink.toureventjava;


import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.PatternTimeoutFunction;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

public class TimeOutJava {
//    private Logger log = LoggerFactory.getLogger(TimeOutJava.class);

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());
        env.setParallelism(1);


        WatermarkStrategy<TourEvent> tourEventWatermarkStrategy = WatermarkStrategy
                .<TourEvent>forBoundedOutOfOrderness(Duration.ofSeconds(5))
                .withTimestampAssigner((event, timestamp) -> event.getTime());

        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.18.240:9092");
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "test666");

        SingleOutputStreamOperator<TourEvent> tourStream = env.addSource(new FlinkKafkaConsumer<String>("ceptest", new SimpleStringSchema(), properties))
//                .filter(s -> s.trim().isEmpty())
                .map((MapFunction<String, TourEvent>) s -> {
                    String[] split = s.split(",");
                    if (split.length > 4)
                        return new TourEvent(Integer.parseInt(split[0]), split[1], Double.parseDouble(split[2]), Long.parseLong(split[3]), Integer.parseInt(split[4]));
                    return new TourEvent(Integer.parseInt(split[0]), split[1], Double.parseDouble(split[2]), Long.parseLong(split[3]), 0);
                })
                .assignTimestampsAndWatermarks(tourEventWatermarkStrategy);

        tourStream.print("tour");
        tourStream.process(new ProcessFunction<TourEvent, TourEvent>() {
            @Override
            public void processElement(TourEvent tourEvent, Context context, Collector<TourEvent> collector) throws Exception {
                long l = context.timerService().currentWatermark();
                System.out.println(l);
            }
        });

        Pattern<TourEvent, TourEvent> pattern = Pattern.<TourEvent>begin("login").where(new IterativeCondition<TourEvent>() {
            @Override
            public boolean filter(TourEvent tourEvent, Context<TourEvent> context) throws Exception {
                return tourEvent.getEventName().equals("登录");
            }
        }).followedByAny("browse").where(new IterativeCondition<TourEvent>() {
            @Override
            public boolean filter(TourEvent tourEvent, Context<TourEvent> context) throws Exception {
                return tourEvent.getEventName().equals("浏览");
            }
        }).followedByAny("order").where(new IterativeCondition<TourEvent>() {
            @Override
            public boolean filter(TourEvent tourEvent, Context<TourEvent> context) throws Exception {
                return tourEvent.getEventName().equals("下单");
            }
        }).followedByAny("exit").where(new IterativeCondition<TourEvent>() {
            @Override
            public boolean filter(TourEvent tourEvent, Context<TourEvent> context) throws Exception {
                return tourEvent.getEventName().equals("退出");
            }
        }).within(Time.seconds(10));

        OutputTag<String> outputTag = new OutputTag<String>("timeout_data"){};

        PatternStream<TourEvent> patternStream = CEP.pattern(tourStream.keyBy((KeySelector<TourEvent, Integer>) TourEvent::getId), pattern);

        SingleOutputStreamOperator<String> selectStream = patternStream.<String,String>select(outputTag, new PatternTimeoutFunction<TourEvent, String>() {
            @Override
            public String timeout(Map<String, List<TourEvent>> map, long time) throws Exception {
                List<TourEvent> loginList = map.get("login");
                String loginEvent = null;
                if (Optional.ofNullable(loginList).isPresent()) {
                    loginEvent=loginList.isEmpty() ? "" : loginList.get(0).toString();
                }

                List<TourEvent> browseList = map.get("browse");
                String browseEvent=null;
                if (Optional.ofNullable(browseList).isPresent()) {
                    browseEvent = browseList.isEmpty() ? "" : browseList.get(0).toString();
                }


                List<TourEvent> orderList = map.get("order");
                String orderEvent=null;
                if (Optional.ofNullable(orderList).isPresent()) {
                    orderEvent = orderList.isEmpty() ? "" : orderList.get(0).toString();
                }

                List<TourEvent> exitList = map.get("exit");
                String exitEvent=null;
                if (Optional.ofNullable(exitList).isPresent()) {
                    exitEvent= exitList.isEmpty() ? "" : exitList.get(0).toString();
                }

                return "超时的Event,时间戳" + time + "," + loginEvent + "\t" + browseEvent + "\t" + orderEvent + "\t" + exitEvent;
            }
        }, new PatternSelectFunction<TourEvent, String>() {
            @Override
            public String select(Map<String, List<TourEvent>> map) throws Exception {
                List<TourEvent> loginList = map.get("login");
                String loginEvent = loginList.isEmpty() ? "" : loginList.get(0).toString();

                List<TourEvent> browseList = map.get("browse");
                String browseEvent = browseList.isEmpty() ? "" : browseList.get(0).toString();

                List<TourEvent> orderList = map.get("order");
                String orderEvent = orderList.isEmpty() ? "" : orderList.get(0).toString();

                List<TourEvent> exitList = map.get("exit");
                String exitEvent = exitList.isEmpty() ? "" : exitList.get(0).toString();

                return "正常的事件" + "," + loginEvent + "\t" + browseEvent + "\t" + orderEvent + "\t" + exitEvent;
            }
        });

        selectStream.print("select");

        DataStream<String> sideOutput = selectStream.getSideOutput(outputTag);
        sideOutput.print("side");

        env.execute();


    }
}
