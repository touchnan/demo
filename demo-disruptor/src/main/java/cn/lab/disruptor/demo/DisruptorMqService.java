package cn.lab.disruptor.demo;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2022/3/20.
 */
public interface DisruptorMqService {
    /**
     * 消息
     * @param message
     */
    void sayHelloMq(String message);
}
