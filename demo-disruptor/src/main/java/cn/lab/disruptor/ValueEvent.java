/*
 * cn.lab.disruptor.ValueEvent.java
 * Aug 30, 2013 
 */
package cn.lab.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * Aug 30, 2013
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class ValueEvent {
    private byte[] packet;

    public byte[] getValue() {
        return packet;
    }

    public void setValue(final byte[] packet) {
        this.packet = packet;
    }

    public final static EventFactory<ValueEvent> EVENT_FACTORY = new EventFactory<ValueEvent>() {
        public ValueEvent newInstance() {
            return new ValueEvent();
        }
    };
}
