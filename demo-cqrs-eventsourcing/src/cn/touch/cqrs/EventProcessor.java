/*
 * cn.touch.cqrs.EventProcessor.java
 * Oct 30, 2013 
 */
package cn.touch.cqrs;

import java.util.ArrayList;
import java.util.List;

/**
 * Oct 30, 2013
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class EventProcessor {
    static List<DomainEvent> list = new ArrayList<DomainEvent>();

    public void process(DomainEvent e) {
        e.process();
        list.add(e);
    }

    interface DomainEvent {
        public void process();
    };
}
