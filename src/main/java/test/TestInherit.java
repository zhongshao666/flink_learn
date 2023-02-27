package test;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

public class TestInherit {
    interface IA{
        void funA();

        //可不实现
        default void funDA(){

        };

        default void funD(){

        };
    }
    interface IB{
        void funB();

        default void funD(){

        };
    }

    //接口多继承
    interface IC extends IA,IB{

        @Override
        default void funD() {

        }
    }

    //抽象类不需要实现
    abstract static class AS implements IA, IB {
        @Override
        public void funA() {

        }

        @Override
        public void funD() {

        }
    }

    static class AA implements IC {
        @Override
        public void funA() {

        }

        @Override
        public void funB() {

        }

        @Override
        public void funDA() {
        }
    }

    static class AB extends AS{

        @Override
        public void funB() {

        }
    }


    public static void main(String[] args) throws Exception{
        CompletableFuture<Integer> integerCompletableFutureA = CompletableFuture.supplyAsync(() -> getSum(1, 50));
        CompletableFuture<Integer> integerCompletableFutureB = CompletableFuture.supplyAsync(() -> getSum(51, 100));
        int sum = integerCompletableFutureA.get() + integerCompletableFutureB.get();
        System.out.println(sum);
    }

    public static int getSum(int i, int j) {
        int sum = 0;
        for (; i <= j; i++) {
            sum+=i;
        }
        return sum;
    }


}


