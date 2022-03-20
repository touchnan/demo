package cn.lab.disruptor.demo;

import cn.lab.disruptor.demo.MessageModel;
import com.lmax.disruptor.EventFactory;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2022/3/20.
 *
 */
public class HelloEventFactory implements EventFactory<MessageModel> {
    @Override
    public MessageModel newInstance() {
        return new MessageModel();
    }
}
