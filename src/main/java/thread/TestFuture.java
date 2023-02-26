package thread;

import java.util.concurrent.*;

public class TestFuture {
    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();
        Task task = new Task();
        FutureTask<Integer> futureTask = new FutureTask<>(task);
        service.submit(futureTask);
        service.shutdown();
        System.out.println("主线程正在执行任务。。。");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            //阻塞直至任务完成
            System.out.println("-------------------------------------");
            System.out.println("执行结果为：" + futureTask.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("任务执行完成。。。");
    }

    static class Task implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            try {
                System.out.println("从线程正在执行任务。。。");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int num = 0;
            for (int i = 0; i <= 100; i++) {
                num += i;
            }
            return num;
        }
    }
}
