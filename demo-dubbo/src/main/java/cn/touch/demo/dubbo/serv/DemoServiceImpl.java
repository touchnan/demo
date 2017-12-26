package cn.touch.demo.dubbo.serv;

import org.springframework.stereotype.Service;

import cn.touch.demo.dubbo.DemoService;

@Service
public class DemoServiceImpl implements DemoService {

	public String hello(String name) {
		System.out.println("provider 被调用:"+name);
		return "Hello "+name;
	}

}
