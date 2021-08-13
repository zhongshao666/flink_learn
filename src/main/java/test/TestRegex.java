package test;

import java.util.concurrent.ScheduledExecutorService;

public class TestRegex {
    public static void main(String[] args){
        String s = "\"aaa\"\"\"bbb\"\"\"";

        String[] split = s.trim().split("[\"]");
        System.out.println(split.length);
        for (String value : split) {
            System.out.println(value);
        }


    }
}
