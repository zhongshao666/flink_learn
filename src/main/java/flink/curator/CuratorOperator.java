package flink.curator;

import org.apache.flink.shaded.curator4.org.apache.curator.RetryPolicy;
import org.apache.flink.shaded.curator4.org.apache.curator.framework.CuratorFramework;
import org.apache.flink.shaded.curator4.org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.flink.shaded.curator4.org.apache.curator.retry.RetryNTimes;

public class CuratorOperator {
    public CuratorFramework client = null;

    public CuratorOperator(String zookeeperPath) {
        RetryPolicy retryPolicy = new RetryNTimes(3, 15000);
        client = CuratorFrameworkFactory.builder().connectString(zookeeperPath).sessionTimeoutMs(15000).retryPolicy(retryPolicy).build();
        client.start();
    }
}
