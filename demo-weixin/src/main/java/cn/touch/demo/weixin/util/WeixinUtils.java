package cn.touch.demo.weixin.util;

import cn.touch.demo.util.ConfigUtils;

/**
 * Created by chengqiang.han on 2017/5/2.
 */
public class WeixinUtils extends ConfigUtils {
//    public static final String APPID = "wx374145b6e229c8de";
//    public static final String APPSECRET = "d4624c36b6795d1d99dcf0547af5443d";
    
    /*-正式号*/
    public static final String APPID = config.getString("wx.mp.appid","wx374145b6e229c8de");
    public static final String APPSECRET = config.getString("wx.mp.appsecret","d4624c36b6795d1d99dcf0547af5443d");


    public static final String OPEN_ID="openid";
    public static final String WEB_ACCESS_TOKEN = "access_token";
    public static final String WEB_REFRESH_TOKEN = "refresh_token";
    
//    public static final String UNION_ID="openid";
    
    public static void main(String[] args) {
		System.out.println(WeixinPayUtils.MCH_ID);
		System.out.println(WeixinPayUtils.UNIFIEDORDER_SIGN_KEY);
		System.out.println(WeixinPayUtils.UNIFIEDORDER_BODY);

		System.out.println(WeixinUtils.APPID);
		System.out.println(WeixinUtils.APPSECRET);
	}
}
