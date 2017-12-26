package cn.touch.guice.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.touch.guice.serv.HelloWorld;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

/**
 * Servlet implementation class HelloWorldServlet
 */
@Singleton
public class HelloWorldServlet extends HttpServlet {
    
    @Inject
    private Injector inj;
	/**
     * 
     */
    private static final long serialVersionUID = -7897769180849629861L;

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    //response.getWriter().append("Hello, guice! "+new Date());
	    inj.getInstance(HelloWorld.class).execute();
	    
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
