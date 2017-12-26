/*
 * cn.touch.servlet3.MyLister.java
 * Jan 25, 2014 
 */
package cn.touch.servlet3;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Jan 25, 2014
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
@WebListener
public class MyLister implements ServletContextListener{

    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
        System.out.println("Start web lister!");
        new Thread(new AsynQueue()).start();
    }
    
}
