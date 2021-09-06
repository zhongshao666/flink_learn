package zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;

public class Test {
    public static void main(String[] args) throws Exception {
//        test1();
//        test2();
//        deleteY();
//        set();
        getY();
    }


    private static void test1() throws KeeperException, InterruptedException {
        ZooKeeper zk = D2Z.getZk();
        //路径
        String path = "/d";
        //文件value值
        byte[] buf = "tom".getBytes();
        //文件权限
        ArrayList<ACL> list = ZooDefs.Ids.OPEN_ACL_UNSAFE;
        //文件持久化保存
        CreateMode p = CreateMode.PERSISTENT;
        //返回路径
        assert zk != null;
        String s = zk.create(path, buf, list, p);
        System.out.println(s);
    }

    //异步
    private static void test2() throws InterruptedException {
        ZooKeeper zk = D2Z.getZk();
        //路径
        String path = "/d2";
        //文件value值
        byte[] buf = "tom666777".getBytes();
        //文件权限
        ArrayList<ACL> list = ZooDefs.Ids.OPEN_ACL_UNSAFE;
        //文件持久化保存
        CreateMode p = CreateMode.PERSISTENT;


        //回调函数，异步执行，创建成功后执行

        //code 状态码  path1 创建路径  ctx create()最后一个参数传递进来的值  name 创建成功后的路径
        AsyncCallback.StringCallback sc = (code, path1, ctx, name) -> {
            KeeperException.Code c = KeeperException.Code.get(code);
            KeeperException.Code ok = KeeperException.Code.OK;
            if (c == ok) {
                System.out.println(code + "-" + path1 + "-" + ctx + "-" + name);
            }
        };
        //当前线程给回调函数传的值

        //返回路径
        assert zk != null;
        zk.create(path, buf, list, p, sc, "66666");
        System.out.println("hhhhhhhhhhhhhhhhh");
        Thread.sleep(1000);
        zk.close();
    }

    private static void delete() throws Exception {
        ZooKeeper zk = D2Z.getZk();
        assert zk != null;
        //参数2为dataVersion
//        zk.delete("/d2", 0);
        //-1为任意版本
        zk.delete("/d2", -1);
    }

    private static void deleteY() throws InterruptedException {
        ZooKeeper zk = D2Z.getZk();
        AsyncCallback.VoidCallback vc = (code, path, ctx) -> {
            System.out.println(code + "-" + path + "-" + ctx);
        };
        zk.delete("/d2", -1, vc, "666666");
        Thread.sleep(1000);
        zk.close();
    }

    private static void set() throws KeeperException, InterruptedException {
        ZooKeeper zk = D2Z.getZk();
        assert zk != null;
        Stat stat = zk.setData("/d2", "hhhh123666".getBytes(), -1);
    }

    private static void get() throws KeeperException, InterruptedException {
        ZooKeeper zk = D2Z.getZk();
        byte[] data = zk.getData("/d2", false, null);
        System.out.println(new String(data));
    }

    private static void getY() throws KeeperException, InterruptedException {
        ZooKeeper zk = D2Z.getZk();
        AsyncCallback.DataCallback dc = (code, path, ctx, value, stat) -> {
            System.out.println(code + path + ctx + new String(value) + stat);
        };
        assert zk != null;
        zk.getData("/d", false, dc, "6666");
        Thread.sleep(1000);
        zk.close();
    }
}
