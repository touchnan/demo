package cn.touch.demo.cloud.springcloudribbon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
@EnableFeignClients
public class SpringCloudRibbonApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudRibbonApplication.class, args);
    }


    @Autowired
    HelloService helloService;

    @RequestMapping(value = "/hello", method = {RequestMethod.GET, RequestMethod.POST})
    public String sayHello() {
        return helloService.hello();
//        return "1";
    }

    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public String sayHome() {
        return helloService.home();
//        return "2";
    }


//	@Bean
//	@LoadBalanced
//	RestTemplate restTemplate() {
//		return new RestTemplate();
//	}
//
//
//	@Autowired
//	HelloServer helloServer;
//
//	@RequestMapping(value = "/hello")
//	public String hello(){
//		return helloServer.getHelloContel();
//	}
//
//	@RequestMapping("/")
//	public String home() {
//		return helloServer.getHomeContel();
//	}
}
