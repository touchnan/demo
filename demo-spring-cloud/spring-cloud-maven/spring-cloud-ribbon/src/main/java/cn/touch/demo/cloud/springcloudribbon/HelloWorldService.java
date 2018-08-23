/**
 * 
 */
package cn.touch.demo.cloud.springcloudribbon;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on Aug 7, 2018.
 *
 */
@FeignClient(value = "config-client")
@Component
public interface HelloWorldService {

	
  @RequestMapping(value = "/hello", method = RequestMethod.GET)
  public String hello();
  
  @RequestMapping(value= "/",method = RequestMethod.GET)
  public String home();
  
}
