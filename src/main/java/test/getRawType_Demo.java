package test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

//getRawType()方法返回声明了这个类型的类或接口，也就是去掉了泛型参数部分的类型对象
public class getRawType_Demo {
    public static void main(String[] args) {
        Grand111 grand = new Grand111();
        Type[] types = grand.getClass().getGenericInterfaces();
        if (types != null) {
            for (Type type : types) {
                if(type instanceof ParameterizedType){
                    System.out.println(((ParameterizedType) type).getRawType());
                    // Output result:
                    // interface test.IA
                    // interface test.IC
                }
            }
        }
    }
}
interface IG<X,Y>{}
interface IA<X,Y>{}
interface IB extends IG{}
interface IC<X>{}
interface ID<X>{}

class Grand111<X> implements IA<String,Integer>,IB,IC<X>,ID{}

