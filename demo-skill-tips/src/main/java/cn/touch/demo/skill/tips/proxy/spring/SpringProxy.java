package cn.touch.demo.skill.tips.proxy.spring;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DelegatingIntroductionInterceptor;

import java.lang.reflect.Method;

/**
 * Created by chengqiang.han on 2018/9/29.
 */
public class SpringProxy {

    static class HelloService {
        public String say() {
            System.out.println("HelloService.say");
            return "hello! I'm " + HelloService.class;
        }
    }

    interface ActService {
        default String act() {
            System.out.println("ActService.act");
            return "action!" + ActService.class;
        }
    }
    class ActIntroduction extends DelegatingIntroductionInterceptor implements ActService {
    }

    public static void main(String[] args) {
//        proxy();
        introduction();
    }

    /**
     * 增加引入，增加没有属性或方法
     */
    public static void introduction() {
        ProxyFactory pf = new ProxyFactory();
        pf.setTarget(new HelloService());
        pf.setOptimize(true);// must be set true
//        pf.addAdvice();// could add other advices;
        pf.addAdvice(new SpringProxy().new ActIntroduction());
        Object proxy = pf.getProxy();
        ((ActService) proxy).act();
        ((HelloService) proxy).say();
    }

    public static void proxy() {
        /**
         * 用到了org.springframework.aop.framework.ProxyFactory，这个内部就是使用了JDK代理或者CGLib代理的技术，
         * 将增强应用到目标类中。Spring定义了org.springframework.aop.framework.AopProxy接口，并提供了两个final的实现类，其中：
         *       Cglib2AopProxy使用CGLib代理技术创建代理，而JdkDynamicAopProxy使用JDK代理技术创建代理；
         *       如果通过ProxyFactory的setInterfaces(Class[] interfaces)指定针对接口进行代理，ProxyFactory就使用JdkDynamicAopProxy；
         *       如果是通过类的代理则使用Cglib2AopProxy，另外也可以通过ProxyFactory的setOptimize(true)方法，
         *           让ProxyFactory启动优化代理模式，这样针对接口的代理也会使用Cglib2AopProxy。
         */
        ProxyFactory pf = new ProxyFactory();
        //设置代理目标
        pf.setTarget(new HelloService());
        //为代理目标添加增强
        pf.addAdvice(new MethodBeforeAdvice() {
            @Override
            public void before(Method method, Object[] args, Object target) throws Throwable {
                System.out.println("method = [" + method + "], args = [" + args + "], target = [" + target + "]");
                System.out.println("SpringProxy.before");
            }
        });
//        pf.addAdvice(,ThrowsAdvice);
        pf.addAdvice(1,new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                Object[] args = invocation.getArguments();// 获取目标方法参数
//                String clientName = (String) args[0];
                System.out.println("How are you: before");
                Object obj = invocation.proceed();
                System.out.println("just enjoy yourself : after");
                return obj;
            }
        });
        pf.addAdvice(2, new AfterReturningAdvice() {
            @Override
            public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
                System.out.println("SpringProxy.afterReturning");
            }
        });

        //生成代理实例
        Object proxy = pf.getProxy();
        System.out.println(proxy.getClass());

        String result = ((HelloService) proxy).say();
        System.out.println(result);
    }
}
