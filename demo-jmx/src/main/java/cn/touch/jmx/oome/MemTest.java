/*
 * cn.touch.jmx.oome.MemTest.java
 * Nov 7, 2013 
 */
package cn.touch.jmx.oome;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Nov 7, 2013
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class MemTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        MemoryWarningSystem.setPercentageUsageThreshold(0.6);

        MemoryWarningSystem mws = new MemoryWarningSystem();
        mws.addListener(new MemoryWarningSystem.Listener() {
            public void memoryUsageLow(long usedMemory, long maxMemory) {
                System.out.println("Memory usage low!!!");
                double percentageUsed = ((double) usedMemory) / maxMemory;
                System.out.println("percentageUsed = " + percentageUsed);
                MemoryWarningSystem.setPercentageUsageThreshold(0.8);
            }
        });

        Collection<Double> numbers = new LinkedList<Double>();
        while (true) {
            numbers.add(Math.random());
        }

    }

}
