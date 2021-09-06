package test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class comparableClassFor_Demo {
    public static void main(String[] args) {
        System.out.println(comparableClassFor(new A()));    // null,A does not implement Comparable.
        System.out.println(comparableClassFor(new B()));    // null,B implements Comparable, compare to Object.
        System.out.println(comparableClassFor(new C()));    // class Demo$C,C implements Comparable, compare to itself.
        System.out.println(comparableClassFor(new D()));    // null,D implements Comparable, compare to its sub type.
        System.out.println(comparableClassFor(new F()));    // null,F is C's sub type.
        System.out.println(comparableClassFor(new String())); //class java.lang.String
    }

    static class A{}
    static class B implements Comparable<Object>{
        @Override
        public int compareTo(Object o) {return 0;}
    }
    static class C implements Comparable<C>{
        @Override
        public int compareTo(C o) {return 0;}

    }
    static class D implements Comparable<E>{
        @Override
        public int compareTo(E o) {return 0;}
    }
    static class E extends D{}
    static class F extends C{}

    /**
     * Returns x's Class if it is of the form "class C implements
     * Comparable<C>", else null.
     */
    static Class<?> comparableClassFor(Object x) {
        if (x instanceof Comparable) {  // 判断是否实现了Comparable接口
            Class<?> c; Type[] ts, as; Type t; ParameterizedType p;
            if ((c = x.getClass()) == String.class)
                return c;   // 如果是String类型，直接返回String.class
            if ((ts = c.getGenericInterfaces()) != null) {  // 判断是否有直接实现的接口
                for (int i = 0; i < ts.length; ++i) {   // 遍历直接实现的接口
                    if (((t = ts[i]) instanceof ParameterizedType) &&   // 该接口实现了泛型
                            ((p = (ParameterizedType)t).getRawType() == // 获取接口不带参数部分的类型对象
                                    Comparable.class) &&   //  该类型是Comparable
                            (as = p.getActualTypeArguments()) != null &&    // 获取泛型参数数组
                            as.length == 1 && as[0] == c)   // 只有一个泛型参数，且该实现类型是该类型本身
                        return c;   // 返回该类型
                }
            }
        }
        return null;
    }
}
