package cn.touch.guice.listener;

import javax.servlet.annotation.WebListener;

import cn.touch.guice.serv.HelloWorld;
import cn.touch.guice.serv.HelloWorldImpl;
import cn.touch.guice.servlet.HelloWorldServlet;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

/**
 * Application Lifecycle Listener implementation class MyGuiceServletContextListener
 *
 */
@WebListener
public class MyGuiceServletContextListener extends GuiceServletContextListener{

    /* (non-Javadoc)
     * @see com.google.inject.servlet.GuiceServletContextListener#getInjector()
     */
    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new ServletModule() {
            

            /* (non-Javadoc)
             * @see com.google.inject.servlet.ServletModule#configureServlets()
             */
            @Override
            protected void configureServlets() {
                serve("/helloworld").with(HelloWorldServlet.class);
                //serveRegex(regex, regexes)
                // TODO Auto-generated method stub
            }
            
        },new Module() {

            @Override
            public void configure(Binder binder) {
                // TODO Auto-generated method stub
                binder.bind(HelloWorld.class).to(HelloWorldImpl.class);
            }
            
        });
    }
	
}
