package cn.touch.demo.weixin.util;

/**
 * Created by chengqiang.han on 2017/5/2.
 */
public class WeixinOAuth2Utils {
    //弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息
    public static final String SNSAPI_USERINFO = "snsapi_userinfo";
    //不弹出授权页面，直接跳转，只能获取用户openid
    public static final String SNSAPI_BASE = "snsapi_base";

    /**微信网页授权获取CODE**/
    public static String WEB_OAUTH_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
    /**微信网页授权获取网页accesstoken和OPENID**/
    public static String WEB_OAUTH_ACCESSTOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
}
