/*
 * cn.lab.disruptor.Test.java
 * Aug 30, 2013 
 */
package cn.lab.disruptor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.SingleThreadedClaimStrategy;
import com.lmax.disruptor.SleepingWaitStrategy;

/**
 * Aug 30, 2013
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class LMAXDisruptor {
    int RING_SIZE = 128;
    private RingBuffer<ValueEvent> ringBuffer;
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        final LMAXDisruptor p = new LMAXDisruptor();
        p.init();
        
        ExecutorService serv = Executors.newFixedThreadPool(200);
        for (int i=0;i<1000000; i++) {
            serv.execute(new Runnable() {
                @Override
                public void run() {
                    p.product();
                }
            });
        }
    }

    private void init() {
        //new SingleThreadedClaimStrategy(RING_SIZE)
        //new MultiThreadedClaimStrategy
        ringBuffer =
                new RingBuffer<ValueEvent>(ValueEvent.EVENT_FACTORY, new SingleThreadedClaimStrategy(RING_SIZE),
                        new SleepingWaitStrategy());
//        new SleepingWaitStrategy();
        //new BlockingWaitStrategy 

        SequenceBarrier barrier = ringBuffer.newBarrier();

        BatchEventProcessor<ValueEvent> eventProcessor =
                new BatchEventProcessor<ValueEvent>(ringBuffer, barrier, new EventHandler<ValueEvent>() {

                    @Override
                    public void onEvent(ValueEvent arg0, long arg1, boolean arg2) throws Exception {
                        // TODO Auto-generated method stub
                        System.out.println(System.currentTimeMillis());
                    }

                });
        ringBuffer.setGatingSequences(eventProcessor.getSequence());
        // only support single thread
        new Thread(eventProcessor).start();
    }

    private void product() {
     // Publishers claim events in sequence
        long sequence = ringBuffer.next();

        // if capacity less than 10%, don't use ringbuffer anymore
        if(ringBuffer.remainingCapacity() < RING_SIZE * 0.1) {
            //log.warn("disruptor:ringbuffer avaliable capacity is less than 10 %");
            // do something
        }
        else {
            ValueEvent event = ringBuffer.get(sequence);
            event.setValue(new byte[]{12,32,33,4}); // this could be more complex with multiple fields
            // make the event available to EventProcessors
            ringBuffer.publish(sequence);
        }
    }
}
