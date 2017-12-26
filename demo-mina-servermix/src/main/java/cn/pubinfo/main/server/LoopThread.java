/*
 * cn.pubinfo.main.server.LoopThread.java
 * May 11, 2014 
 */
package cn.pubinfo.main.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * May 11, 2014
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class LoopThread extends Thread {
    private static Logger log = LoggerFactory.getLogger(LoopThread.class);

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        while (true) {
            try {
                // String msg = (String) Main.queue.take();

                if (Main.queue.isEmpty()) {
                    TimeUnit.MILLISECONDS.sleep(500);
                } else {
                    Collection<Object> c = new ArrayList<Object>();
                    Main.queue.drainTo(c,500);
                    // String msg = (String) Main.queue.poll();
                    
                   if (!c.isEmpty()) {
                       Object[][] params = new Object[c.size()][2];
                       int i=0;
                       for (Iterator<Object> iter = c.iterator(); iter.hasNext(); i++) {
                           Object msg = iter.next();
                           if (msg!=null) {
                               params[i] = ((String) msg).trim().split(",");
                           }
                           //log.info(((String) msg).trim());
                       }
                       
                       DBUtils.getInstance().batchUpdate("INSERT INTO `sms_report` (`content`, `sendto`) VALUES (?, ?)", params);
                       //DBUtils.getInstance().batchUpdate("INSERT INTO `tab_m` (`col_msg`, `col_phone`) VALUES (?, ?)", params);
                   }
//                    for (Object msg : c) {
//                        if (log.isInfoEnabled()) {
//                            if (msg != null) {
//                                // log.info("INSERT INTO `tab_test` (name) VALUES ('{}') ",msg.trim());
//                                log.info(((String) msg).trim());
//                            }
//                        }
//                    }
                    
                }
            } catch (InterruptedException e) {
                log.error("Take Queue error", e);
            }
        }
    }

}
