/*
 * cn.touch.demo.schedul.MyServlet.java
 * Nov 19, 2013 
 */
package cn.touch.demo.schedule;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Nov 19, 2013
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class MyServlet extends HttpServlet {
    public void init(ServletConfig cfg) {
        String key = "org.quartz.impl.StdSchedulerFactory.KEY";
        ServletContext servletContext = cfg.getServletContext();
        StdSchedulerFactory factory = (StdSchedulerFactory) servletContext.getAttribute(key);
        try {
            Scheduler quartzScheduler = factory.getScheduler("MyQuartzScheduler");
        } catch (SchedulerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // TODO use quartzScheduler here.
    }
}
