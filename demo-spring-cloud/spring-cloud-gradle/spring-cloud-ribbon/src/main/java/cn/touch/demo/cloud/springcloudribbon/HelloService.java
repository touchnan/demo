package cn.touch.demo.cloud.springcloudribbon;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by chengqiang.han on 2018/8/8.
 */
@FeignClient(value = "config-client")
@Component
public interface HelloService {
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello();

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home();
}
