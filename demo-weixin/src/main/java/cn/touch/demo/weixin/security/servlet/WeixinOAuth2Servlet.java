package cn.touch.demo.weixin.security.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.touch.demo.weixin.util.Constants;
import cn.touch.demo.weixin.util.WeixinUtils;

/**
 * Created by chengqiang.han on 2017/5/2.
 */
@WebServlet(asyncSupported = true, name = "weixinOAuth2Servlet", displayName = "WeixinOAuth2Servlet", urlPatterns = {"/auth2/*"})
public class WeixinOAuth2Servlet
        extends HttpServlet {
	private static final long serialVersionUID = -668900545583325993L;
	
	private static Logger logger = LoggerFactory.getLogger(WeixinOAuth2Servlet.class);
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doBusiness(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	this.doBusiness(request, response);
    }

    private void doBusiness(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCharacterEncoding(request, response);
        String openid = (String) request.getSession().getAttribute("openid");
        if (StringUtils.isBlank(openid)) {
            String code = request.getParameter("code");
            String state = request.getParameter("state");

            //认证成功后返回票据code和自定义state状态
            //redirect_uri/?code=CODE&state=STATE。
            //code说明 ： code作为换取access_token的票据，每次用户授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期。
            if (StringUtils.isNotBlank(code)) {
                //第二步：通过票据code换取网页授权access_token
                //获取code后，请求以下链接获取access_token：  https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
                String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx99976c98f817fd21&secret=5e538f3c2184b404f3d4f7fabaccd9dd&code=" + code + "&grant_type=authorization_code";

            /*
            String requestUrl = WeixinOAuth2Utils.WEB_OAUTH_ACCESSTOKEN_URL;
            requestUrl = requestUrl.replace("APPID", WeixinUtils.APPID);
            requestUrl = requestUrl.replace("SECRET", WeixinUtils.APPSECRET);
            requestUrl = requestUrl.replace("CODE", code);
            */
                JSONObject results = Constants.httpRequest(url, "GET", null);
                logger.info("oauth2 token:{}",JSON.toJSONString(results));
                openid = (String) results.get(WeixinUtils.OPEN_ID);
                String access_token = (String) results.get(WeixinUtils.WEB_ACCESS_TOKEN);
                String refresh_token = (String) results.get(WeixinUtils.WEB_REFRESH_TOKEN);
                request.getSession().setAttribute("openid", openid);
                request.getSession().setAttribute("access_token", access_token);
                request.getSession().setAttribute("refresh_token", refresh_token);



                //第三步：刷新access_token（如果需要）

                //第四步：拉取用户信息(需scope为 snsapi_userinfo)

                //https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN
                url = "https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid+"&lang=zh_CN ";

                results = Constants.httpRequest(url, "GET", null);
                logger.info("oauth2 get userinfo:{}",JSON.toJSONString(results));
//                WxUser user = new WxUser();
                request.getSession().setAttribute("nickname", results.get("nickname"));
                request.getSession().setAttribute("unionid", results.get("unionid"));
                request.getSession().setAttribute("sex", results.get("sex"));
                request.getSession().setAttribute("province", results.get("province"));
                request.getSession().setAttribute("city", results.get("city"));
                request.getSession().setAttribute("country", results.get("country"));
            }
            
            request.getRequestDispatcher("/userinfo.jsp").forward(request,response);
        } else {
//            response.getWriter().write("openid:");
//            response.getWriter().write(openid);
//            response.getWriter().write("<br>");
            request.getRequestDispatcher("/userinfo.jsp").forward(request,response);
        }

    }

    private void setCharacterEncoding(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        /*request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-store");
        response.setDateHeader("Expires", 0);*/
    }

    /**
     * 取客户端post上来的参数
     *
     * @param request
     * @return
     * @throws ServletException
     * @throws IOException
     */
    private String getInputStreamParameter(HttpServletRequest request) throws ServletException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
        String line = null;
        StringBuffer sb = new StringBuffer();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}