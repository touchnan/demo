package cn.touch.demo.bigdata.client.zookeeper.curator.discovery2;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceCache;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.details.ServiceCacheListener;

/**
 * Created by <a href="mailto:88052350@qq.com">touchnan</a> on 2016/11/30.
 */
public class Client {
    private static final String PATH = "/discovery/example";

    public static void main(String[] args) throws Exception {

        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", new ExponentialBackoffRetry(1000, 3));
        client.start();

        //创建服务发现
//        ServiceDiscovery serviceDiscovery = ServiceDiscoveryBuilder.builder(InstanceDetails.class).client(client).
//                basePath(PATH).serializer(serializer).build();
        ServiceDiscovery serviceDiscovery = ServiceDiscoveryBuilder.builder(InstanceDetails.class).client(client).
                basePath(PATH).build();

        serviceDiscovery.start();


        //服务变更通知
        final ServiceCache cache = serviceDiscovery.serviceCacheBuilder().name("s1").build();
        cache.addListener(new ServiceCacheListener() {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
            }

            @Override
            public void cacheChanged() {
                System.out.println("changed:" + cache.getInstances().size());
            }
        });
        cache.start();

    }
}
