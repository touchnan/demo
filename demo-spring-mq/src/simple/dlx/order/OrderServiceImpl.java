package cn.lab.spring.demo.mq.dlx.order;

import com.alibaba.fastjson2.JSON;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2022/7/12.
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Value(value = "${xiaojie.order.exchange}")
    private String orderExchange;
    @Value(value = "${xiaojie.order.routingKey}")
    private String orderRoutingKey;

    @Override
    public String saveOrder(Order order) {
        String orderId = UUID.randomUUID().toString();
        order.setOrderId(orderId);
        order.setOrderName("test");
        order.setPayMoney(3000D);
        Integer result = orderMapper.addOrder(order);
        if (result > 0) {
//            String msg = JSONObject.toJSONString(order);
            String msg = JSON.toJSONString(order);
            //发送mq
            sendMsg(msg, orderId);
            return "success";
        }
        return "fail";
    }

    /**
     * @description: 发送mq消息
     * @param:
     * @param: msg
     * @param: orderId
     * @return: void
     * @author xiaojie
     * @date: 2021/10/8 22:33
     */
    @Async //异步线程发送 ，此处需要单独创建一个类去创建该方法，不然该异步线程可能不会生效
    public void sendMsg(String msg, String orderId) {
        rabbitTemplate.convertAndSend(orderExchange, orderRoutingKey, msg, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //设置过期时间30s
                message.getMessageProperties().setExpiration("30000");
//                message.getMessageProperties().setMessageId(orderId);
                return message;
            }
        });
    }

    @Override
    public Order getByOrderId(String orderId) {
        return orderMapper.getOrder(orderId);
    }

    @Override
    public Integer updateOrderStatus(String orderId) {
        return orderMapper.updateOrder(orderId);
    }
}
