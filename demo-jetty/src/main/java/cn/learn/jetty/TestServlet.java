/*
 * cn.learn.jetty.TestServlet.java
 * Sep 23, 2013 
 */
package cn.learn.jetty;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Sep 23, 2013
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
@Controller
public class TestServlet {
    private static Logger log = Logger.getLogger(TestServlet.class);

    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public void ping(HttpServletResponse response) throws IOException {
        log.info("ping page is called");
        IOUtils.write("Embedded Jetty Server is Up and Running", response.getOutputStream());
    }

}
