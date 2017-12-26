/*
 * demo.cn.touch.M.java
 * Jul 5, 2014 
 */
package demo.cn.touch;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

/**
 * Jul 5, 2014
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class M {

    /**
     * @param args
     * @throws UnsupportedEncodingException 
     */
    public static void main(String[] args) throws UnsupportedEncodingException {
        // TODO Auto-generated method stub
        List<NameValuePair> nvps = new ArrayList<NameValuePair>(2);
        nvps.add(new BasicNameValuePair("name","hcq"));
        nvps.add(new BasicNameValuePair("passwd","1234567"));
        
//        StringEntity myEntity = new StringEntity("{a:2,b:3}", "UTF-8");
//        new HttpConnector().post4Stream("http://localhost:8080/yx-plat/services/http/remote/userPerms", "{a:2,b:3}");
        new HttpConnector().post("http://localhost:8080/yx-plat/services/http/remote/userPerms",nvps);
        new HttpConnector().get("http://localhost:8080/yx-plat/services/http/remote/userPerms",nvps);
    }

}
