package test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

//ParameterizedType接口还有一个getOwnerType()方法，如果该类型是一个内部类/接口，返回它的外部类/接口。如果该类型不是内部类/接口，返回null。
public class getOwnerType_Demo {
    public static void main(String[] args) {
        Outer<String> outer = new Outer<>();
        Outer<String>.Child<Integer> child = outer.new Child<Integer>();
        Type type = child.getClass().getGenericSuperclass();
        if(type instanceof ParameterizedType){
            System.out.println(((ParameterizedType) type).getOwnerType()); //Outer<X>
        }
    }
}

class Outer<X>{
    class Inner<Y>{}
    class Child<Y> extends Inner<Y>{}
}