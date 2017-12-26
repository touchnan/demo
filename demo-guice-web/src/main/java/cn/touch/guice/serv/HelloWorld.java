/**
 * 
 */
package cn.touch.guice.serv;

import java.io.IOException;

import com.google.inject.ImplementedBy;

/**
 * Nov 11, 2014
 *
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 */
//@ImplementedBy(HelloWorldImpl.class)
public interface HelloWorld {
    void execute() throws IOException;
}
