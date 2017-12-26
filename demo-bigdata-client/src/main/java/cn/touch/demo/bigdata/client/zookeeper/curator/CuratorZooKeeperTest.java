package cn.touch.demo.bigdata.client.zookeeper.curator;

import org.apache.curator.test.TestingServer;

/**
 * Created by <a href="mailto:88052350@qq.com">touchnan</a> on 2016/11/30.
 */
public class CuratorZooKeeperTest {
    public static void main(String[] args) throws Exception {
        TestingServer server = new TestingServer(2181);
        server.start();
    }
}
