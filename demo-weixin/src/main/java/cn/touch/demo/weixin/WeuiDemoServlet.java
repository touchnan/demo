/**
 * 
 */
package cn.touch.demo.weixin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on Nov 8, 2017.
 *
 */

@WebServlet(name="uiCoreServlet",urlPatterns = "/weui/*",loadOnStartup = 1,asyncSupported = true)
public class WeuiDemoServlet extends HttpServlet {
	private static final long serialVersionUID = 1562323256583342315L;
	private final static Logger logger = LoggerFactory.getLogger(WeuiDemoServlet.class);

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//    	if ("/".equals(req.getRequestURI())) {
//    		req.getRequestDispatcher("/index.jsp").forward(req,resp);
//    	} else if (req.getRequestURI().startsWith("/weui/")) {
//    		req.getRequestDispatcher(req.getRequestURI()+".jsp").forward(req,resp);
		
//		System.out.println(req.getRequestURI());
//		System.out.println(req.getContextPath());
//		System.out.println(req.getServletPath());
		String page = "/ui"+req.getPathInfo()+".jsp";
		req.getRequestDispatcher(page).forward(req,resp);
//    	} 
	}
	
}
