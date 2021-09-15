package test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;

//ParameterizedType是Type接口的子接口，表示参数化的类型，即实现了泛型参数的类型。需要注意：
//如果直接用bean对象instanceof ParameterizedType，结果都是false。
//Class对象不能instanceof ParameterizedType，编译会报错。
//只有用Type对象instanceof ParameterizedType才能得到想要的比较结果。可以这么理解：一个Bean类不会是ParameterizedType，只有代表这个Bean类的类型（Type）才可能是ParameterizedType。
//实现泛型参数，可以是给泛型传入了一个真实的类型，或者传入另一个新声明的泛型参数；只声明泛型而不实现，instanceof ParameterizedType为false。
public class ParameterizedType_Demo {

    public static void main(String[] args) {
        HashSet<Object> objects = new HashSet<>();
        Grand111 grand = new Grand111();
        Type[] types = grand.getClass().getGenericInterfaces();
        if (types != null) {
            for (Type type : types) {
                System.out.println(type.getTypeName() + " " + (type instanceof ParameterizedType));
                // Output result:
                //IA<java.lang.String, java.lang.Integer> true  实现了泛型参数的类型,将泛型参数化了
                //IB false
                //IC<X> true
                //ID false
            }
        }

        Grand6 child1 = new Child6();
        Grand6 child2_1 = new Child62();
        Grand6 child2_2 = new Child62<String, String>();
        Child62<String, String> child2_3 = new Child62<String, String>();
        Child63<String, String> child3 = new Child63<String,String>();
        System.out.println(child1 instanceof ParameterizedType);    // flase
        System.out.println(child2_1 instanceof ParameterizedType);  // flase
        System.out.println(child2_2 instanceof ParameterizedType);  // flase
        System.out.println(child2_3 instanceof ParameterizedType);  // flase
        System.out.println(child1.getClass().getGenericSuperclass() instanceof ParameterizedType);  // true
        System.out.println(child2_1.getClass().getGenericSuperclass() instanceof ParameterizedType);    // true
        System.out.println(child3.getClass().getGenericSuperclass() instanceof ParameterizedType);  // flase
        // System.out.println(child1.getClass() instanceof ParameterizedType);  // Complie Errors


    }
}
//interface IG<X,Y>{}
//interface IA<X,Y>{}
//interface IB extends IG{}
//interface IC<X>{}
//interface ID<X>{}
//class Grand111<X> implements IA<String,Integer>,IB,IC<X>,ID{}
class Grand6{}
class Super6<A,B> extends Grand6{}
class Child6 extends Super6<String,String>{}
class Child62<A,B> extends Super6<A,B>{}
class Child63<A,B> extends Super6{}




