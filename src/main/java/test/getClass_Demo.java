package test;

//与instanceof相应对的是getClass()方法，无论该对象如何转型，getClass()返回的只会是它的运行时类型，可以简单的理解为它的实际类型，也就是new它的时候的类型。
//有一种例外情况，匿名对象。当匿名对象调用getClass()时返回的是依赖它的对象的运行时类型，并以1,2,3…的索引区分。
public class getClass_Demo {
    public static void main(String[] args) {
        D d = new D();
        System.out.println(new A(){}.getClass());   // class Demo$1
        System.out.println(new B(){}.getClass());   // class Demo$2
        System.out.println(new Comparable<Object>(){    // class Demo$3
            @Override
            public int compareTo(Object o) {
                return 0;
            }}.getClass());
        System.out.println(d.c.getClass()); // class D$1
        System.out.println(new C(){}.getClass());
        System.out.println(new C(){}.getClass());
    }
}

abstract class A{}
abstract class B{}
abstract class C{}
class D{
    C c;
    D(){
        c= new C(){};
    }
}

