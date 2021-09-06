package test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

//与getRawType()相对应，getActualTypeArguments()以数组的形式返回泛型参数列表。
//注意，这里返回的是实现该泛型时传入的参数，可以看下方代码的打印结果：
//当传入的是真实类型时，打印的是全类名。
//当传入的是另一个新声明的泛型参数时 ，打印的是代表该泛型参数的符号。
public class getActualTypeArguments_Demo {
    public static void main(String[] args) {
        Grand111 grand = new Grand111();
        Type[] types = grand.getClass().getGenericInterfaces();
        if (types != null) {
            for (Type type : types) {
                if(type instanceof ParameterizedType){
                    System.out.println(type.getTypeName());
                    Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
                    if(typeArguments != null){
                        for (Type typeArg : typeArguments) {
                            System.out.println(typeArg.getTypeName());
                            // Output result:
                            // IA<java.lang.String, java.lang.Integer>
                            // java.lang.String
                            // java.lang.Integer
                            // IC<X>
                            // X
                        }
                    }
                }
            }
        }
    }

}
//interface IG<X,Y>{}
//interface IA<X,Y>{}
//interface IB extends IG{}
//interface IC<X>{}
//interface ID<X>{}
//class Grand111<X> implements IA<String,Integer>,IB,IC<X>,ID{}