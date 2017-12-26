/*
 * cn.lab.disruptor.ThreadLocalBoundedMQ.java
 * Aug 30, 2013 
 */
package cn.lab.disruptor;

/**
 * Aug 30, 2013
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class ThreadLocalBoundedMQ {
    private int MAX_BUFFER_TIME=222;
    private int BATCH_INS_COUNT = 222;
    
    private long lastFlushTime=0L;
    
    private byte[][] msgs=new byte[BATCH_INS_COUNT][];
    
    private int offset=0;
    
    public byte[][] getMsgs(){
        return msgs;
    }
    
    public void addMsg(byte[] msg)
    {
        msgs[offset++]=msg;
    }

    public int size() {
        return offset;
    }

    public void clear() {
        offset=0;
        lastFlushTime=System.currentTimeMillis();
    }
    
    public boolean needFlush(){
        return (System.currentTimeMillis()-lastFlushTime > MAX_BUFFER_TIME)
        && offset>0;
    }
}
