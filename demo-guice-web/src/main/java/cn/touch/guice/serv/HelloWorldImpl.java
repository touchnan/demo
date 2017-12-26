/**
 * 
 */
package cn.touch.guice.serv;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;

/**
 * Nov 11, 2014
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 */
@RequestScoped
public class HelloWorldImpl implements HelloWorld {
    private HttpServletRequest req;
    private HttpServletResponse resp;

    @Inject
    public HelloWorldImpl(HttpServletRequest request,
            HttpServletResponse response) {
        super();
        this.req = request;
        this.resp = response;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.touch.guice.serv.HelloWorld#execute()
     */
    @Override
    public void execute() throws IOException {
        String name = req.getParameter("user");
        if (name == null || name.length() < 1)
            name = "Guest";
        resp.getWriter()
                .append(String.format(
                        "Hello, %s. %s -> sessionId=%s,hashCode=%d ", name,
                        new Date(), req.getSession().getId(), hashCode()));

    }

}
