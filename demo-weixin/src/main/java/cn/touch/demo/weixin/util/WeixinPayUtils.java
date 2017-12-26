package cn.touch.demo.weixin.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import cn.touch.demo.util.ConfigUtils;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2017/5/8.
 */
public class WeixinPayUtils extends ConfigUtils{

    public static final String PAY_URL_TONG_YI_XIA_DANG = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    public static final String  MCH_ID = config.getString("wx.pay.mch_id");//微信支付商户号

//    public static final String UNIFIEDORDER_NOTIFY_URL = "/busi/pay/rcb/payrecall";
    public static final String UNIFIEDORDER_NOTIFY_URL = "/wx/rcb";

    public static final String UNIFIEDORDER_SIGN_KEY = config.getString("wx.pay.sign.key");

    public static final String UNIFIEDORDER_BODY=config.getString("wx.pay.unifiedorder.body");

    public static final String SUCCESS = "SUCCESS";
    
    
	public static String getServerDomainName() {
		return config.getString("server.domainname", "");
	}
	
	public static String getDomainNameRootContextPath(HttpServletRequest request) {
		return request.getScheme() + "://" + (StringUtils.isNotBlank(getServerDomainName())?getServerDomainName():request.getServerName()) + request.getContextPath();
	}

}
