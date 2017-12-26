package cn.pubinfo.mina.client;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet implementation class ClientServlet
 */
@WebServlet(name = "clientServlet", loadOnStartup = 0, urlPatterns = { "/client" }, asyncSupported = true)
public class ClientServlet extends HttpServlet {
    private static final long serialVersionUID = -6662399182508349352L;
    private final static Logger log = LoggerFactory.getLogger(ClientServlet.class);
    private AtomicLong count = new AtomicLong();

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ClientServlet() {
        super();
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
        long c = count.incrementAndGet();
        Client.getInstance().send(String.format("18900000000,测试短信%s", c));
        String q= request.getParameter("q");
        if ("q".equals(q)) {
            Client.getInstance().closeSession();
        }
        
//      StringBuffer sbf = new StringBuffer();
//      sbf.append(String.format(Locale.CHINESE, "%04d", new java.util.Random().nextInt(9999)));
//      int i = sbf.toString().getBytes().length;
//      if (i!=4) {
//          System.out.println(sbf.toString());
//          throw new RuntimeException("error");
//      }
//      System.out.println(i);        
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.GenericServlet#destroy()
     */
    @Override
    public void destroy() {
        super.destroy();
        Client.getInstance().closeSession();
    }

}
