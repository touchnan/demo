package cn.lab.spring.demo.mq.dlx.order;

import cn.lab.spring.demo.mq.dlx.order.Order;
import cn.lab.spring.demo.mq.dlx.order.OrderService;
import cn.lab.spring.demo.mq.dlx.order.OrderStatus;
import com.alibaba.fastjson2.JSONObject;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2022/7/12.
 * 死信队列解决订单超时问题
 */

@Component(value = "aa")
@RabbitListener(bindings = @QueueBinding(
        value = @Queue("xiaojie_order_dlx_queue"),
        exchange = @Exchange(value = "xiaojie_order_dlx_exchange", type = ExchangeTypes.DIRECT),
        key = "order.dlx"))
//@Slf4j
public class Consumer {
    @Autowired
    private OrderService orderService;

    /*
     * @param msg
     * @param headers
     * @param channel
     * @死信队列消费消息，如果订单状态是未支付，则修改订单状态
     * @return void
     */
    @RabbitHandler
    public void handlerMsg(@Payload String msg, @Headers Map<String, Object> headers,
                           Channel channel) throws IOException {
//        log.info("接收到的消息是direct：{}" + msg);
        System.out.println(String.format("接收到的消息是direct：{}" + msg));
        try {
//            Order orderEntity = JSONObject.parseObject(msg, Order.class);
            Order orderEntity = JSONObject.parseObject(msg, Order.class);
            if (orderEntity == null) {
                return;
            }
            // 根据订单号码查询该笔订单是否存在
            Order order = orderService.getByOrderId(orderEntity.getOrderId());
            if (order == null) {
                return;
            }
            //判读订单状态
            if (OrderStatus.UNPAY.getStatus() == order.getStatus()) {
                //未支付，修改订单状态
                orderService.updateOrderStatus(orderEntity.getOrderId());
                //库存+1
                System.out.println("库存+1");
            }
            //delivery tag可以从消息头里边get出来
            Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
            //手动应答，消费者成功消费完消息之后通知mq，从队列移除消息，需要配置文件指明。第二个参数为是否批量处理
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            e.printStackTrace();
            //补偿机制
        }
    }
}
