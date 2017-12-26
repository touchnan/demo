/*
 * cn.touch.axon.MyBaseCommandGateway.java
 * Oct 31, 2013 
 */
package cn.touch.axon;

import java.util.concurrent.TimeUnit;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandDispatchInterceptor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;

/**
 * Oct 31, 2013
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class MyBaseCommandGateway extends DefaultCommandGateway  {

    /**
     * @param commandBus
     * @param commandDispatchInterceptors
     */
    public MyBaseCommandGateway(CommandBus commandBus, CommandDispatchInterceptor[] commandDispatchInterceptors) {
        super(commandBus, commandDispatchInterceptors);
        // TODO Auto-generated constructor stub
    }
}
