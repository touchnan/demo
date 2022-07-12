package cn.lab.spring.demo.mq.rabbit;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2022/7/11.
 */
@RestController
public class RabbitDemoTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("/test")
    public void send(){
        Message message = new Message("Hello,消息".getBytes(StandardCharsets.UTF_8));
//        message.setKey("rabbit");
//        message.setValue("Hello");
        System.out.println("消息发送时间：" + Consumer.getCurrentTime());
        rabbitTemplate.convertAndSend(RabbitMQConfig.DELAY_EXCHANGE, "queue.delay", message, new MessagePostProcessor() {
            @Override
            public org.springframework.amqp.core.Message postProcessMessage(org.springframework.amqp.core.Message message) throws AmqpException {
                message.getMessageProperties().setContentEncoding("UTF-8");
                message.getMessageProperties().setExpiration("20000");
                return message;
            }
        });
    }
}
