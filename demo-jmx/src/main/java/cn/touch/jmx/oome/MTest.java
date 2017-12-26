/*
 * cn.touch.jmx.oome.MTest.java
 * Nov 7, 2013 
 */
package cn.touch.jmx.oome;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.util.List;

/**
 * Nov 7, 2013
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class MTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        List<MemoryPoolMXBean> mps = ManagementFactory.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean mp : mps) {
            System.out.println(mp.getName() +" ["+ mp.getType() +"]:"+mp.getUsage().getMax()/1024/1024);
        }
    }

}
