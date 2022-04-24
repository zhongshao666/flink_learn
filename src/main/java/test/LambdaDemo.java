package test;

import com.google.common.base.Supplier;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class LambdaDemo {
    public static void main(String[] args) {
        Callable<Integer> callable = () -> 42;
        Map map;
        Collection collection;
        new HashMap<>();
    }
}
