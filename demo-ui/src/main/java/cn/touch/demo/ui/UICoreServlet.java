package cn.touch.demo.ui;


import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2017/7/6.
 */
@WebServlet(name="uiCoreServlet",urlPatterns = "/ui",loadOnStartup = 1,asyncSupported = true)
public class UICoreServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cmd = req.getParameter("c");
        String name = req.getParameter("n");
        String path = req.getParameter("p");
        System.out.println("cmd = " + cmd);
        System.out.println("name = " + name);
        System.out.println("path = " + path);
        if (StringUtils.isNotBlank(path)) {
        	name = path+"/"+name;
        }

        if ("jsp".equalsIgnoreCase(cmd)) {
            req.getRequestDispatcher("/WEB-INF/view/jsp/"+name+".jsp").forward(req,resp);
        } else {
        	//resp.setCharacterEncoding("UTF-8");
        	resp.setContentType("application/json; charset=utf-8");
//        	System.out.println(new File(name).getAbsolutePath());
//        	IOUtils.write(FileUtils.readFileToByteArray(new File(name)), resp.getWriter());
        	IOUtils.copyLarge(this.getClass().getClassLoader().getResourceAsStream(name), resp.getOutputStream());
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(" POSt == = " );
        super.doPost(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doOptions(req, resp);
    }
}
