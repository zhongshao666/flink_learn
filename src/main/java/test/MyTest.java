package test;

import org.junit.Test;

import java.util.Optional;

public class MyTest {
    @Test
    public void test(){
        String other = Optional.of("time").filter(s -> !"time".equals(s)).orElse(null);
        System.out.println(other);
    }
}
