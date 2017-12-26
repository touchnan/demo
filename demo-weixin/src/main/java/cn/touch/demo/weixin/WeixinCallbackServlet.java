package cn.touch.demo.weixin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.touch.demo.weixin.util.WeixinPayUtils;

@WebServlet(name = "weiXinCallbackServlet", urlPatterns = "/wx/rcb", loadOnStartup = 1, asyncSupported = true)
public class WeixinCallbackServlet extends WeixinServlet {
	private final static Logger logger = LoggerFactory.getLogger(WeixinCallbackServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		payCallback(req,resp,req.getSession());
	}
	
	private void payCallback(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String sm = getInputStreamParameter(request);// xml
		if (StringUtils.isNotBlank(sm)) {
			try {
				String ret = parseCallback(request, sm);
				response.getWriter().write(ret);
				response.getWriter().flush();
			} catch (IOException e) {
				logger.error("微信支付结果通知处理失败", e);
			}
		}
	}

	private String getInputStreamParameter(HttpServletRequest request) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
			String line = null;
			StringBuffer sb = new StringBuffer();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch (IOException e) {
			logger.error("read input stream", e);
		}
		return "";
	}

	private String parseCallback(HttpServletRequest request, String sm) {
		if (logger.isDebugEnabled()) {
			logger.debug("微信支付结果通知收到信息:{}", sm);
		}

		Map<String, String> responseMap = new HashMap<>();
		responseMap.put("return_code", "FAIL");// 失败
		Map<String, String> resultMap = this.doParseXml(sm);
		String retSign = resultMap.get("sign");
		resultMap.remove("sign");

		if (!this.sign4UnifiedOrder(resultMap, WeixinPayUtils.UNIFIEDORDER_SIGN_KEY).equals(retSign)) {
			// 签名有误，可能被黑，不处理
			responseMap.put("return_msg", "签名失败");
			logger.error("微信支付结果通知签名有误");
		} else {
			String out_trade_no = resultMap.get("out_trade_no");
			String openid = resultMap.get("openid");
			String total_fee = resultMap.get("total_fee");
			String cash_fee = resultMap.get("cash_fee");
			String time_end = resultMap.get("time_end");
			String transaction_id = resultMap.get("transaction_id");
			String bank_type = resultMap.get("bank_type");

			responseMap.put("return_code", "SUCCESS");
			responseMap.put("return_msg", "OK");
		}
		String sign = this.sign4UnifiedOrder(responseMap, WeixinPayUtils.UNIFIEDORDER_SIGN_KEY);
		responseMap.put("sign", sign);
		return this.xml2UnifiedOrder(responseMap);
	}
}
