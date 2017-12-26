package cn.touch.demo.weixin.security.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 * Created by chengqiang.han on 2017/5/2.
 */
//@javax.servlet.annotation.WebServlet(name = "WeixinServlet",asyncSupported=true,displayName="myserv",urlPatterns={"/wx/*"})
public class WeixinServlet extends HttpServlet {
	private static final long serialVersionUID = -2058983363139965366L;
	private static Logger logger = LoggerFactory.getLogger(WeixinServlet.class);
    private static final String WEIXIN_ACCESS_TOKEN = "qScMNXv9Gy60";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        System.out.println("------------------");
        setCharacterEncoding(request, response);
        System.out.println("=================");
        String content = getInputStreamParameter(request);
        System.out.println(content);
        System.out.println("!!!!!!!!!!!!!!!!!");
        if (logger.isDebugEnabled()) {
            logger.debug(content);
        }
    }

    private String getInputStreamParameter(HttpServletRequest request)
            throws ServletException, IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
        String line = null;
        StringBuffer sb = new StringBuffer();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    private void setCharacterEncoding(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException
    {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        if (logger.isDebugEnabled()) {
            logger.debug(JSON.toJSONString(request.getParameterMap()));
        }
        String signature = request.getParameter("signature");

        String timestamp = request.getParameter("timestamp");

        String nonce = request.getParameter("nonce");


        String echostr = request.getParameter("echostr");
        if (checkSignature(WEIXIN_ACCESS_TOKEN, signature, timestamp, nonce))
        {
            response.getWriter().write(echostr);
            response.getWriter().close();
        }
    }

    private static boolean checkSignature(String token, String signature, String timestamp, String nonce)
    {
        String[] arr = { token, timestamp, nonce };

        Arrays.sort(arr);
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
        }
        MessageDigest md = null;
        String tmpStr = null;
        try
        {
            md = MessageDigest.getInstance("SHA-1");

            byte[] digest = md.digest(content.toString().getBytes());

            tmpStr = Hex.encodeHexString(digest);
        }
        catch (NoSuchAlgorithmException e)
        {
            logger.error("SHA-1 encode :", e);
        }
        content = null;

        return tmpStr != null ? tmpStr.equals(signature) : false;
    }

    public static void main(String[] args)
    {
        String signature = "993f015bbbbc8bdad4d9d19e5bce229f6a7bbe9d";

        String timestamp = "1493278477";

        String nonce = "1780064321";


        String echostr = "4084030913743150879";
        checkSignature("qScMNXv9Gy60", signature, timestamp, nonce);
    }
}
