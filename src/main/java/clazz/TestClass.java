package clazz;

import java.lang.reflect.Method;

public class TestClass {
    public static void main(String[] args) {
        show("yerasel.Person");
    }

    private static void show(String name) {
        try {
            // JVM将使用类A的类装载器,将类A装入内存(前提是:类A还没有装入内存),不对类A做类的初始化工作
            Class<Person> classtype3 = Person.class;
            // 获得classtype中的方法
            Method getMethod3 = classtype3.getMethod("getName", new Class[] {});
            Class[] parameterTypes3 = { String.class, int.class };
            Method setMethod3 = classtype3
                    .getMethod("setName", parameterTypes3);

            // 实例化对象，因为这一句才会输出“静态初始化”以及“初始化”
            Object obj3 = classtype3.newInstance();
            // 通过实例化后的对象调用方法
            getMethod3.invoke(obj3); // 获取默认值
            setMethod3.invoke(obj3, "Setting new ", 3); // 设置
            getMethod3.invoke(obj3); // 获取最新
            System.out.println("----------------");

            // 返回运行时真正所指的对象
            Person p = new Person();
            Class<? extends Person> classtype = p.getClass();// Class.forName(name);
            // 获得classtype中的方法
            Method getMethod = classtype.getMethod("getName", new Class[] {});
            Class[] parameterTypes = { String.class, int.class };
            Method setMethod = classtype.getMethod("setName", parameterTypes);
            getMethod.invoke(p);// 获取默认值
            setMethod.invoke(p, "Setting new ", 1); // 设置
            getMethod.invoke(p);// 获取最新
            System.out.println("----------------");

            // 装入类,并做类的初始化
            Class<?> classtype2 = Class.forName(name);
            // 获得classtype中的方法
            Method getMethod2 = classtype2.getMethod("getName", new Class[] {});
            Class[] parameterTypes2 = { String.class, int.class };
            Method setMethod2 = classtype2
                    .getMethod("setName", parameterTypes2);
            // 实例化对象
            Object obj2 = classtype2.newInstance();
            // 通过实例化后的对象调用方法
            getMethod2.invoke(obj2); // 获取默认值
            setMethod2.invoke(obj2, "Setting new ", 2); // 设置
            getMethod2.invoke(obj2); // 获取最新

            System.out.println("----------------");

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
