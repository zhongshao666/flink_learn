package zookeeper;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

public class D2Z {
    public static ZooKeeper getZk(){
        try {
            CountDownLatch cdl = new CountDownLatch(1);
            Watcher watcher= watchedEvent -> {
                Watcher.Event.KeeperState state = watchedEvent.getState();
                Watcher.Event.KeeperState sc = Watcher.Event.KeeperState.SyncConnected;
                if (sc == state) {
                    System.out.println("连接成功");
                    cdl.countDown();
                }else {
                    System.out.println("连接失败");
                }
            } ;

            ZooKeeper zk = new ZooKeeper("192.168.18.253:2181", 1000,watcher );
            cdl.await();
            return zk;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
