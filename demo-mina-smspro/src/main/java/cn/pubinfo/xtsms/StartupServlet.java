package cn.pubinfo.xtsms;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.pubinfo.xtsms.queue.QueueManager;
import cn.pubinfo.xtsms.serv.ServerManager;

/**
 * Servlet implementation class StartupServlet
 */
//@WebServlet(name = "StartupServlet", loadOnStartup = 0, urlPatterns = { "/admin" }, asyncSupported = true)
public class StartupServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private ServerManager manager = null;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StartupServlet() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.GenericServlet#init()
     */
    @Override
    public void init() throws ServletException {
        super.init();
        startup();
    }

    private void startup() {
        if (manager==null) {
            manager = new ServerManager();
            
            manager.start();
        }
    }
    
    /**
     * @see Servlet#destroy()
     */
    public void destroy() {
        shutdown();
        super.destroy();
    }
    
    private void shutdown() {
        if (manager!=null) {
            manager.shutdown();
            manager = null;
        }
    }

    
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        response.setCharacterEncoding("utf8");
        String cmd = request.getParameter("cmd");
        if ("SHUTDOWN".equalsIgnoreCase(cmd)) {
            shutdown();
        } else if ("REBOOT".equalsIgnoreCase(cmd)) {
                startup();
        } else if ("INFO".equalsIgnoreCase(cmd)) {
            PrintWriter pw = response.getWriter();
            if (manager==null) {
                pw.write("服务关闭");
                if (!QueueManager.sendQueue.isEmpty()
                        || !QueueManager.statusQueue.isEmpty()
                        || !QueueManager.receiveQueue.isEmpty()) {
                    pw.write(",正处理队列中数据... 请稍后关闭服务器进程");
                }
            } else {
                pw.write("服务运行");
            }
            pw.write("; 发送短信队列为空:"+QueueManager.sendQueue.isEmpty());
            pw.write("; 反馈短信发送状态队列为空:"+QueueManager.statusQueue.isEmpty());
            pw.write("; 收到短信队列为空:"+QueueManager.receiveQueue.isEmpty());
            pw.flush();
        } else {
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

}
