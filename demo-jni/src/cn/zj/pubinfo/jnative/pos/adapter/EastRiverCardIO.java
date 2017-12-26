/*
 * YSLAdaptor.java.YSLAdaptor.java
 * Dec 29, 2010
 */
package cn.zj.pubinfo.jnative.pos.adapter;

import cn.zj.pubinfo.jnative.pos.ICardIO;

/**
 * Dec 29, 2010
 * 
 * @author <a href="mailto:touchnan@gmail.com">chengqiang.han</a>
 * 
 */
public class EastRiverCardIO extends CardIO implements ICardIO {
     
    private String name;
    /* (non-Javadoc)
     * @see cn.zj.pubinfo.jnative.pos.ICardIO#seekCard()
     */
    @Override
    public boolean seekCard() {
        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * 
     */
    private void see() {
        
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    

}
