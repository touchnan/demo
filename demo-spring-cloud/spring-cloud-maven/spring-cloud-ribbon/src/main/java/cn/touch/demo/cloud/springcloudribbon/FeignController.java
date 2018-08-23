/**
 * 
 */
package cn.touch.demo.cloud.springcloudribbon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on Aug 7, 2018.
 *
 */

@RestController
public class FeignController {
    @Autowired 
    HelloWorldService helloWorldService;
    
    @RequestMapping(value = "/hello",method = {RequestMethod.GET,RequestMethod.POST})
    public String sayHello(){
        return helloWorldService.hello();
    }
    
    @RequestMapping(value = "/",method = {RequestMethod.GET,RequestMethod.POST})
    public String sayHome(){
        return helloWorldService.home();
    }
}
