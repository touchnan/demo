package cn.touch.demo.logback.layout;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.Converter;

/**
 * Created by <a href="mailto:88052350@qq.com">touchnan</a> on 2016/12/19.
 */
public class JsonPatternLayout extends PatternLayout {
    Converter<ILoggingEvent> head;

    @Override
    protected String writeLoopOnConverters(ILoggingEvent event) {
//        return super.writeLoopOnConverters(event);
        StringBuilder buf = new StringBuilder(128);
        Converter<ILoggingEvent> c = head;
        while (c != null) {

            c.write(buf, event);
            c = c.getNext();
        }
        return buf.toString();
    }
}
