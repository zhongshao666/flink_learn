package test;

import java.io.Serializable;
import java.lang.reflect.Type;

//返回的一定是接口。
//必然是该类型自己实现的接口，继承过来的不算。
public class getGenericInterfaces_Demo {
    public static void main(String[] args) {
        Grand child = new Child();
        //getSuperclass()返回的是直接父类的类型，不包括泛型参数。
        //getGenericSuperclass()返回的是包括泛型参数在内的直接父类。
        Type[] types = child.getClass().getGenericInterfaces();
        if (types != null) {
            for (Type type : types) {
                System.out.println(type.getTypeName());
            }
        }
    }
}

abstract class Grand implements Comparable<Grand> {
}

abstract class Super extends Grand implements Serializable {
}

class Child extends Super implements Cloneable {
    public int compareTo(Grand o) {
        return 0;
    }
}
