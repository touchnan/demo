package cn.lab.spring.demo.mq.queue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.DelayQueue;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2022/7/11.
 *
 * 订单超时取消之延迟队列
 * https://www.csdn.net/tags/MtTaEgzsNzEwMDU2LWJsb2cO0O0O.html
 */
public class DelayQueueTest {
    public static void main(String[] args){
        //创建一个延迟队列
        DelayQueue<DelayTask> delayQueue = new DelayQueue<DelayTask>();
        DelayQueueTest delayQueueTest = new DelayQueueTest();
        //生产者放入消息
        delayQueueTest.producer(delayQueue);
        //消费者消费消息
        delayQueueTest.consumer(delayQueue);

    }

    //定义一个消费者，启动一个线程，循环从队列中拿元素
    private void consumer(DelayQueue<DelayTask> delayTasks){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        DelayTask delayTask = delayTasks.take();
                        System.out.println("消息消费时间：" + getCurrentTime() + ",msg:" + delayTask.getMsg());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    //生产者，将消息带上延迟时间，放入延迟队列
    private void producer(DelayQueue<DelayTask> delayTasks){
        DelayTask delayTask = new DelayTask(5000,"delay msg");
        System.out.println("消息放入时间：" + getCurrentTime());
        delayTasks.add(delayTask);
    }

    public static String getCurrentTime(){
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }
}
