package cn.touch.demo.bigdata.client.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;

/**
 * Created by <a href="mailto:88052350@qq.com">touchnan</a> on 2016/11/30.
 */
public class CuratorTest {
    public static void main(String[] args) throws Exception {
        TestingServer server = new TestingServer();
        server.start();

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        client.start();
        client.blockUntilConnected();

    }
}
