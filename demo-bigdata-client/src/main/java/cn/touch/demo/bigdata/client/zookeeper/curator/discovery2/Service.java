package cn.touch.demo.bigdata.client.zookeeper.curator.discovery2;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.util.Collection;

/**
 * Created by <a href="mailto:88052350@qq.com">touchnan</a> on 2016/11/30.
 */
public class Service {
    private static final String PATH = "/discovery/example";

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", new ExponentialBackoffRetry(1000, 3));
        client.start();

        //生成可用服务实例
        UriSpec uriSpec = new UriSpec("{scheme}://foo.com:{port}");
//        ServiceInstance thisInstance = ServiceInstance.builder().name(serviceName).payload(new InstanceDetails(description))
//                .port((int) (65535 * Math.random())) //in a real application, you'd use a common port
//                .uriSpec(uriSpec).build();

                ServiceInstance thisInstance = ServiceInstance.builder().name("serviceName").payload(new InstanceDetails("description"))
                .port((int) (65535 * Math.random())) //in a real application, you'd use a common port
                .uriSpec(uriSpec).build();


        JsonInstanceSerializer serializer = new JsonInstanceSerializer(InstanceDetails.class);
        //添加服务实例
//        ServiceDiscovery serviceDiscovery = ServiceDiscoveryBuilder.builder(InstanceDetails.class)
//                .client(client).basePath(path).serializer(serializer).thisInstance(thisInstance).build();
//        serviceDiscovery.start();

        ServiceDiscovery serviceDiscovery = ServiceDiscoveryBuilder.builder(InstanceDetails.class)
                .client(client).basePath(PATH).serializer(serializer).thisInstance(thisInstance).build();
        serviceDiscovery.start();

        //查询服务配置
        try {
            Collection<String> serviceNames = serviceDiscovery.queryForNames();
            System.out.println(serviceNames.size() + " type(s)");
            for (String serviceName : serviceNames) {
                Collection<ServiceInstance> instances = serviceDiscovery.queryForInstances(serviceName);
                System.out.println(serviceName);
                for (ServiceInstance instance : instances) {
//                    outputInstance(instance);
                }
            }
        } finally {
            CloseableUtils.closeQuietly(serviceDiscovery);
        }
    }
}
