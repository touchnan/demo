package cn.touch.demo.skill.tips.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Created by chengqiang.han on 2018/9/29.
 */
public class JdkProxy {

    interface HelloService {
        default String say() {
            return "I'm " + HelloService.class;
        }
    }


    Object createProxy(Object target) {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), (proxy, method, args) -> {
//            InvocationHandler
            System.out.println("jdk proxy 方法拦截，java.lang.reflect.InvocationHandler");
            System.out.println("o = [" + proxy.getClass() + "], method = [" + method.getName() + "], args = [" + args + "]");
            //把方法委派给目标对象，这里是helloService
            Object result = method.invoke(target, args);
            System.out.println("result = " + result);
            return result;
        });
    }

    public static void main(String[] args) {
        HelloService helloService = new HelloService() {
        };
        HelloService proxy = (HelloService) new JdkProxy().createProxy(helloService);
        proxy.say();
    }
}
