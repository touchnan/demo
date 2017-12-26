package cn.touch.demo.logback.encoder;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.encoder.EncoderBase;

import java.io.IOException;

/**
 * Created by <a href="mailto:88052350@qq.com">touchnan</a> on 2016/12/16.
 */
public class JsonEncoder extends EncoderBase<ILoggingEvent> {
    @Override
    public void doEncode(ILoggingEvent event) throws IOException {

    }

    @Override
    public void close() throws IOException {

    }
}
