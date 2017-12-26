package cn.touch.demo.embed;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by <a href="mailto:88052350@qq.com">touchnan</a> on 2016/11/14.
 */
@RunAs("special")
@WebServlet(urlPatterns = {"/","/test/*"}, name="AnnotationTest", initParams={@WebInitParam(name="fromAnnotation", value="xyz")})
@DeclareRoles({"user","client"})
public class AnnotationTest extends HttpServlet {
    private DataSource myDS;

    @Resource(mappedName="UserTransaction")
    private UserTransaction myUserTransaction;

    @Resource(mappedName="maxAmount")
    private Double maxAmount;


    @Resource(mappedName="jdbc/mydatasource")
    public void setMyDatasource(DataSource ds)
    {
        myDS=ds;
    }


    @PostConstruct
    private void myPostConstructMethod ()
    {
        System.err.println("PostConstruct called");
    }


    @PreDestroy
    private void myPreDestroyMethod()
    {
        System.err.println("PreDestroy called");
    }

    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
        {
            response.setContentType("text/html");
            ServletOutputStream out = response.getOutputStream();
            out.println("<html>");
            out.println("<body>");
            out.println("<h1>Results</h1>");
            out.println(myDS.toString());
            out.println("<br/>");
            out.println(maxAmount.toString());
            out.println("</body>");
            out.println("</html>");
            out.flush();
        }
        catch (Exception e)
        {
            throw new ServletException(e);
        }
    }
}
