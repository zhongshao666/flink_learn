package test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.function.BiConsumer;

public class TestHashMap {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        //阈值大于容量时，挂在链表上了
        HashMap<Integer, String> hashMap = new HashMap<>(16,1.5f);
        hashMap.put(1, "zhou");
        hashMap.put(2, "hao");
        hashMap.put(3, "hhh");

        hashMap.forEach(new BiConsumer<Integer, String>() {
            @Override
            public void accept(Integer integer, String s) {
                System.out.println(integer+"="+s);
            }
        });
        for (int i = 0; i < 30; i++) {
            System.out.println(i);
            hashMap.put(i, "zhou");

            Class hashMapClass = hashMap.getClass();
            Field loadFactor = hashMapClass.getDeclaredField("loadFactor");
            loadFactor.setAccessible(true);
            System.out.println("loadFactor:" + loadFactor.get(hashMap));
            Field threshold = hashMapClass.getDeclaredField("threshold");
            threshold.setAccessible(true);
            System.out.println("threshold:" + threshold.get(hashMap));
            Method capacity = hashMapClass.getDeclaredMethod("capacity");
            capacity.setAccessible(true);
            System.out.println("Class:capacity:" + capacity);
            System.out.println("capacity:"+capacity.invoke(hashMap));

        }
        new HashMap<>();

//        System.out.println(hashMap);
//        System.out.println(Float.isNaN(0.75f));

    }
}
