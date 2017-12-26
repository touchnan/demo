package cn.touch.demo.weixin;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import cn.touch.demo.util.Constants;
import cn.touch.demo.weixin.util.WeixinPayUtils;
import cn.touch.demo.weixin.util.WeixinUtils;

@WebServlet(name = "weiXinPayServlet", urlPatterns = "/wx/pay", loadOnStartup = 1, asyncSupported = true)
public class WeiXinPayServlet extends WeixinServlet {
	private final static Logger logger = LoggerFactory.getLogger(WeiXinPayServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Map<String, String> paraMap = h5xiaDang(req);
		resp.getWriter().write(JSON.toJSONString(paraMap));
		
//		resp.setContentType("text/html;charset=utf-8");  
		  
//	        /** 设置响应头允许ajax跨域访问 **/  
//		resp.setHeader("Access-Control-Allow-Origin", "*");  
//	        /* 星号表示所有的异域请求都可以接受， */  
//		resp.setHeader("Access-Control-Allow-Methods", "GET,POST");
//		resp.setContentType("application/json; charset=utf-8");

//		resp.getWriter().write("{\"mweb_url\":\"https://wx.tenpay.com/cgi-bin/mmpayweb-bin/checkmweb?prepay_id=wx2017100918030970020e24e20130447829&package=4130145068\"}");
		resp.getWriter().flush();
	}

	public Map<String, String> h5xiaDang(HttpServletRequest request) {
		// 并向微信下商户支付订单
		String mweb_url = unifiedOrderWiexinPay(request);
		if (StringUtils.isNotBlank(mweb_url)) {
			// 生成并返回H5页面调用支付界面凭证
			Map<String, String> paraMap = new HashMap<String, String>();
//			paraMap.put("appId", WeixinUtils.APPID);// 公众号名称，由商户传入
//			paraMap.put("timeStamp", Long.toString(System.currentTimeMillis() / 1000));// 时间戳，自1970年以来的秒数
//			paraMap.put("nonceStr", Constants.digestMD5(java.util.UUID.randomUUID().toString()).toUpperCase());// 随机串
//			paraMap.put("package", "prepay_id=" + prepay_id);
//			paraMap.put("signType", "MD5");
//			String sign = this.sign4UnifiedOrder(paraMap, WeixinPayUtils.UNIFIEDORDER_SIGN_KEY);
//			paraMap.put("paySign", sign);
			paraMap.put("mweb_url", mweb_url);
			return paraMap;
		}
		return Collections.emptyMap();
	}

	private String getSerialNumber() {
//		return java.util.UUID.randomUUID().toString();
			SimpleDateFormat sdf = new SimpleDateFormat("SSSyyMMddHHmmss");
			return "8" + StringUtils.leftPad("1001", 6, "0") + sdf.format(new Date());
	}

	private String unifiedOrderWiexinPay(HttpServletRequest request) {

		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("appid", WeixinUtils.APPID);
		paraMap.put("mch_id", WeixinPayUtils.MCH_ID);
		// paraMap.put("device_info","web");//自定义参数，可以为终端设备号(门店号或收银设备ID)，PC网页或公众号内支付可以传"WEB"
		paraMap.put("nonce_str", Constants.digestMD5(java.util.UUID.randomUUID().toString()).toUpperCase());// 随机字符串，长度要求在32位以内

		String serialNumber = getSerialNumber();
		paraMap.put("attach", getSerialNumber());// 附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用

		paraMap.put("body", WeixinPayUtils.UNIFIEDORDER_BODY);
		paraMap.put("out_trade_no", serialNumber);// 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@
													// ，且在同一个商户号下唯一

		paraMap.put("total_fee", Long.toString(1));// 订单总金额，单位为分，
		paraMap.put("spbill_create_ip", Constants.getIpAddr(request));// APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。

		paraMap.put("trade_type", "MWEB");// JSAPI,MWEB

		paraMap.put("notify_url",
				WeixinPayUtils.getDomainNameRootContextPath(request) + WeixinPayUtils.UNIFIEDORDER_NOTIFY_URL);// 此路径是微信服务器调用支付结果通知路径

		paraMap.put("limit_pay", "no_credit");// 上传此参数no_credit--可限制用户不能使用信用卡支付
		// paraMap.put("openid", me.getOpenid());///
		// trade_type=JSAPI时（即公众号支付），此参数必传，此参数为微信用户在商户对应appid下的唯一标识

		paraMap.put("scene_info",
				"{\"h5_info\":{\"type\":\"Wap\",\"wap_url\":\"http://web.dyzx7.cn/h5\",\"wap_name\":\"得意在线-房卡购买\"}}");

		/** 订单失效时间，测试用 */
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date now = new Date();
		paraMap.put("time_start", sdf.format(now));// 订单生成时间
		now.setMinutes(now.getMinutes() + 30);
		paraMap.put("time_expire", sdf.format(now));// 订单失效时间

		String sign = sign4UnifiedOrder(paraMap, WeixinPayUtils.UNIFIEDORDER_SIGN_KEY);
		paraMap.put("sign", sign);// 签名

		String paraXml = xml2UnifiedOrder(paraMap);
		if (logger.isDebugEnabled()) {
			logger.debug("调用微信的下单接口请求参数信息{}", paraXml);
		}
		String retXml = Constants.sendHttp2(WeixinPayUtils.PAY_URL_TONG_YI_XIA_DANG, "POST", paraXml);
		if (logger.isDebugEnabled()) {
			logger.debug("调用微信的下单接口返回结果信息{}", retXml);
		}

		Map<String, String> retMap = doParseXml(retXml);
		if (WeixinPayUtils.SUCCESS.equals(retMap.get("return_code"))) {// 通信成功
			if (logger.isDebugEnabled()) {
				logger.debug("调用微信的下单接口返回通信成功");
			}

			// 需要验证一把签名
			String retSign = retMap.get("sign");
			retMap.remove("sign");
			if (this.sign4UnifiedOrder(retMap, WeixinPayUtils.UNIFIEDORDER_SIGN_KEY).equals(retSign)) {
				if (WeixinPayUtils.SUCCESS.equals(retMap.get("result_code"))) {// 业务成功
					if (logger.isDebugEnabled()) {
						logger.debug("调用微信的下单接口返回业务成功");
					}
					return retMap.get("mweb_url");
				} else {
					String errCode = retMap.get("err_code");
					String errInfo = retMap.get("err_code_des");
					if (logger.isDebugEnabled()) {
						logger.debug("调用微信的下单接口返回业务失败{{}:{}}", errCode, errInfo);
					}
				}
			} else {
				logger.error("调用微信的下单接口返回信息签名错误");
			}

		} else {
			String errInfo = retMap.get("return_msg");
			if (logger.isDebugEnabled()) {
				logger.debug("调用微信的下单接口返回通信失败:{}", errInfo);
			}
		}

		return null;
	}

}
