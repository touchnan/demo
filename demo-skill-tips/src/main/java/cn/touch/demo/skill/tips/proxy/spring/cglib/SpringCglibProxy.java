package cn.touch.demo.skill.tips.proxy.spring.cglib;

import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by chengqiang.han on 2018/9/29.
 *
 * spring中，如果有接口则用JDK动态代理，没接口，类代理则用cglib
 */
public class SpringCglibProxy {

    static  class HelloService {
        public String say() {
            return "hello! I'm " + HelloService.class;
        }
    }

    public Object createBeanProxy(Object target) {
        Enhancer enhancer = new Enhancer(); // 该类用于生成代理对象
        enhancer.setSuperclass(target.getClass()); // 设置目标类为代理对象的父类
        enhancer.setCallback(new MethodInterceptor() {// 设置回调逻辑，这里是回调方法拦截
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                //somethings to do, 方法拦截，可以做些业务逻辑处理
                System.out.println("spring-cglib方法拦截，调用org.springframework.cglib.proxy.Callback");
                System.out.println("o = [" + o.getClass() + "], method = [" + method.getName() + "], objects = [" + objects + "], methodProxy = [" + methodProxy.getClass() + "]");
                //把方法委派给目标对象，这里是helloService
                Object result = methodProxy.invoke(target, objects);
                System.out.println("result = " + result);
                return result;
            }
        });
        return enhancer.create();//通过字节码技术动态创建子类实例,返回代理,这里是helloService的代理
    }

    public static void main(String[] args) {
        HelloService helloService = new HelloService();
        System.out.println("helloService.getClass() = " + helloService.getClass());
        HelloService proxy = (HelloService) new SpringCglibProxy().createBeanProxy(helloService);
        proxy.say();
    }
}
