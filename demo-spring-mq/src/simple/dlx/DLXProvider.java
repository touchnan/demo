package cn.lab.spring.demo.mq.dlx;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2022/7/12.
 */
public class DLXProvider {
    //定义交换机
    private static final String MY_DIRECT_EXCHANGE = "snail_direct_exchange";
    //普通队列路由键
    private static final String DIRECT_ROUTING_KEY = "msg.send";

    @Autowired
    private RabbitTemplate rabbitTemplate;
    public String sendDlxMsg(){
        String msg="我是模拟死信队列的消息。。。。。";
        rabbitTemplate.convertAndSend(MY_DIRECT_EXCHANGE, DIRECT_ROUTING_KEY, msg, (message) -> {
            //设置有效时间，如果消息不被消费，进入死信队列
            message.getMessageProperties().setExpiration("10000");
            return message;
        });
        return "success";
    }
}
