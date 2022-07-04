package thread;





import com.google.common.util.concurrent.ThreadFactoryBuilder;

import javax.annotation.Nonnull;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
* @Description  TODO 自定义线程池
**/
public class CustomThreadPoolExecuter {

    /**
     * @Description TODO  定时执行线程池
     **/
    public static ScheduledExecutorService newScheduledThreadPool(int coreSize, String threadNamePre) {
        return new ScheduledThreadPoolExecutor(coreSize, getCustomThreadFactory(threadNamePre));
    }


    /**
     * @Description TODO 固定长度线程池
     **/
    public static ExecutorService newFixThreadPool(int n, String threadNamePre) {
        return new ThreadPoolExecutor(n, n, 0L, TimeUnit.SECONDS, new SynchronousQueue<>(), getCustomThreadFactory(threadNamePre));
    }

    /**
     * @Description TODO 指定增长线程池
     **/
    public static ExecutorService newGrowThreadPool(int coreSize, int maxSize, String threadNamePre) {
        return new ThreadPoolExecutor(coreSize, maxSize, 60L, TimeUnit.SECONDS, new SynchronousQueue<>(), getCustomThreadFactory(threadNamePre));
    }


    public static ThreadFactory getCustomThreadFactory(String threadNamePre) {
        return new ThreadFactory() {
            private final AtomicInteger count = new AtomicInteger(0);
            @Override
            public Thread newThread(@Nonnull Runnable r) {
                Thread t = new Thread(r);
                String threadName = threadNamePre + "-" + CustomThreadPoolExecuter.class.getSimpleName() + "-" + count.addAndGet(1);
                t.setName(threadName);
                return t;
            }
        };
    }

    public static void main111(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = CustomThreadPoolExecuter.newGrowThreadPool(4, 8, "lac");
        Future<Integer> submit = executorService.submit(new Task());
        System.out.println(submit.get());
        executorService.shutdown();

    }

    static class Task implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            System.out.println("子线程开始计算-"+Thread.currentThread().getName());
            int sum = 0;
            for (int i = 0; i <= 100; i++) {
                sum += i;
            }
            return sum;

        }
    }

    public static void main (String[] args) throws Exception{
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("thread-pool-%d").build();
        ExecutorService executorService = new ThreadPoolExecutor(10, 16, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100), threadFactory, new ThreadPoolExecutor.AbortPolicy());
        Future<Integer> submit =executorService.submit(new Task());
        System.out.println(submit.get());
        executorService.shutdown();
    }
}
