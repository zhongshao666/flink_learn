package thread;





import javax.annotation.Nonnull;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
* @Description  TODO 自定义线程池
**/
public class CustomThreadPoolExecuter {

    /**
    * @Description  TODO  定时执行线程池
    **/
    public static ScheduledExecutorService newScheduledThreadPool(int coreSize, String threadNamePre) {
        return new ScheduledThreadPoolExecutor(coreSize, getCustomThreadFactory(threadNamePre));
    }


    /**
    * @Description  TODO 固定长度线程池
    **/
    public static ExecutorService newFixThreadPool(int n, String threadNamePre) {
        return new ThreadPoolExecutor(n, n, 0L, TimeUnit.SECONDS, new SynchronousQueue<>(), getCustomThreadFactory(threadNamePre));
    }

    /**
    * @Description  TODO 指定增长线程池
    **/
    public static ExecutorService newGrowThreadPool(int coreSize, int maxSize, String threadNamePre) {
        return new ThreadPoolExecutor(coreSize, maxSize, 60L, TimeUnit.SECONDS, new SynchronousQueue<>(),getCustomThreadFactory(threadNamePre));
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

}
