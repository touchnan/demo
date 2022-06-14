package cn.lab.disruptor.demo.config;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2022/3/20.
 * 获取实例化对象
 */
@Component
public class BeanManager implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }
}
