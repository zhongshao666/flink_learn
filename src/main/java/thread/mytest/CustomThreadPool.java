package thread.mytest;


import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.HashSet;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class CustomThreadPool {
    public static void main(String[] args){

//        System.out.println(Dish.res.size());
//        System.out.println(Dish.res);
        ExecutorService threadPool = Executors.newCachedThreadPool();
        //8491

//        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("thread-pool-%d").build();
//        ExecutorService threadPool = new ThreadPoolExecutor(4, 8, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10000), threadFactory, new ThreadPoolExecutor.AbortPolicy());
        //4 8 1024 11084
        SmallTool.printTimeAndThread("小白和小伙伴们 进餐厅点菜");
        long startTime = System.currentTimeMillis();

        CompletableFuture[] dishes = IntStream.rangeClosed(1, 48)// 6:2802 12:4185  48:11628  48:12012  128:7352  128:6579
                .mapToObj(i -> new Dish("菜" + i, 1))
                .map(dish -> CompletableFuture.runAsync(dish::makeUseCPU, threadPool))
                .toArray(size -> new CompletableFuture[size]);

        CompletableFuture.allOf(dishes).join();

        threadPool.shutdown();

        SmallTool.printTimeAndThread("菜都做好了，上桌 " + (System.currentTimeMillis() - startTime));

        System.out.println(Dish.res.size());
//        for (Long re : Dish.res) {
//            System.out.println(re);
//        }
        System.out.println(new HashSet<>(Dish.res).toString());

    }
}
