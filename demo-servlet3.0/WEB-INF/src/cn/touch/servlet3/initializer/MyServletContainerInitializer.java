/**
 * 
 */
package cn.touch.servlet3.initializer;

import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on Aug 24, 2018.
 *
 */
@HandlesTypes(MyWebContainerInitializer.class)
public class MyServletContainerInitializer implements ServletContainerInitializer {

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContainerInitializer#onStartup(java.util.Set, javax.servlet.ServletContext)
	 */
	@Override
	public void onStartup(Set<Class<?>> webAppInitializerClasses, ServletContext arg1) throws ServletException {
		// TODO Auto-generated method stub
		System.out.println("invoke when web container initialize!");
		
		if (webAppInitializerClasses != null) {
			for (Class<?> waiClass : webAppInitializerClasses) {
				System.out.println(waiClass);
			}
		}
	}

}
