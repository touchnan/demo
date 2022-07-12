package cn.lab.spring.demo.mq.rabbit;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2022/7/11.
 */
@Component
public class Consumer {
    @RabbitHandler
    @RabbitListener(queues = "buss.queue")
    public void consumerMessage(Message message){
//        String key = message.getKey();
//        String value = message.getValue();
        System.out.println("延迟队列消费时间" + getCurrentTime());
//        System.out.println("消费的消息：" + message.getKey() + "---" + message.getValue());
        System.out.println("消费的消息：" + new String(message.getBody()));
    }

    public static String getCurrentTime() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }
}
