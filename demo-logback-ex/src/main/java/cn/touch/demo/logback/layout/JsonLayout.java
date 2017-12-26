package cn.touch.demo.logback.layout;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.LayoutBase;
import com.alibaba.fastjson.JSON;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by <a href="mailto:88052350@qq.com">touchnan</a> on 2016/12/16.
 */
public class JsonLayout extends LayoutBase<ILoggingEvent>{
    @Override
    public String doLayout(ILoggingEvent loggingEvent) {
        Map<String, Object> map = new LinkedHashMap<String, Object>(0);
        Map<String, Object> source = new LinkedHashMap<String, Object>(0);


//        source.put("method", loggingEvent.getLocationInformation().getMethodName());
//        source.put("class", loggingEvent.getLocationInformation().getClassName());
//        source.put("file", loggingEvent.getLocationInformation().getFileName());
//        source.put("line", safeParse(loggingEvent.getLocationInformation().getLineNumber()));
//
//        map.put("timeMillis", loggingEvent.getTimeStamp());
//        map.put("thread", loggingEvent.getThreadName());
//        map.put("level", loggingEvent.getLevel().toString());
//        map.put("loggerName", loggingEvent.getLocationInformation().getClassName());
//        map.put("source", source);
//        map.put("endOfBatch", false);
//        map.put("loggerFqcn", loggingEvent.getFQNOfLoggerClass());


        map.put("message", safeToString(loggingEvent.getMessage()));
        map.put("thrown", formatThrowable(loggingEvent));

        return JSON.toJSONString(map);
    }

    private static String safeToString(Object obj) {
        if (obj == null) return null;
        try {
            return obj.toString();
        } catch (Throwable t) {
            return "Error getting message: " + t.getMessage();
        }
    }

    private static Integer safeParse(String obj) {
        try {
            return Integer.parseInt(obj.toString());
        } catch (NumberFormatException t) {
            return null;
        }
    }

    private List<Map<String, Object>> formatThrowable(ILoggingEvent le) {
        if (le.getThrowableProxy() == null ||
                le.getThrowableProxy().getCause() == null)
            return null;
        List<Map<String, Object>> traces = new LinkedList<Map<String, Object>>();
        Map<String, Object> throwableMap = new LinkedHashMap<String, Object>(0);

        StackTraceElementProxy[] stackTraceElementProxyArray = le.getThrowableProxy().getStackTraceElementProxyArray();
        for(StackTraceElementProxy sProxy : stackTraceElementProxyArray) {
            StackTraceElement stackTraceElement  = sProxy.getStackTraceElement();
            throwableMap.put("class", stackTraceElement.getClassName());
            throwableMap.put("file", stackTraceElement.getFileName());
            throwableMap.put("line", stackTraceElement.getLineNumber());
            throwableMap.put("method", stackTraceElement.getMethodName());
            throwableMap.put("location", "?");
            throwableMap.put("version", "?");
            traces.add(throwableMap);
        }

        return traces;
    }
}
