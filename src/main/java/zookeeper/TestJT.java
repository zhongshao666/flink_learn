package zookeeper;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class TestJT {
    public static void main(String[] args) throws Exception{
        t2();
    }
    private static void t1()throws Exception{
        ZooKeeper zk = D2Z.getZk();
        //c1绑定事件监听，创建了就输出
        zk.exists("/c1",watchedEvent -> {
            Watcher.Event.EventType type = watchedEvent.getType();
            Watcher.Event.EventType et = Watcher.Event.EventType.NodeCreated;
            if (type==et){
                //监听到创建操作
                System.out.println(watchedEvent.getPath()+"创建了");
            }
        });

        Thread.sleep(60000);
        zk.close();
    }
    public static void t2()throws Exception{
        ZooKeeper zk = D2Z.getZk();
        zk.exists("b1",watchedEvent -> {
            Watcher.Event.EventType type = watchedEvent.getType();
            Watcher.Event.EventType ch = Watcher.Event.EventType.NodeDataChanged;
            Watcher.Event.EventType del = Watcher.Event.EventType.NodeDeleted;
            if (type==ch){
                System.out.println("修改了权限");
            }else if (type==del){
                System.out.println("删除了");
            }else {
                System.out.println("创建了");
            }
        });
        Thread.sleep(60000);
        zk.close();
    }
}