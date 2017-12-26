package cn.touch.demo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by chengqiang.han on 2017/5/2.
 */
public class Constants {
	private static Logger logger = LoggerFactory.getLogger(Constants.class);

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}

		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}

		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		
		return ip.split(",")[0];
	}

	public static boolean isGameAccount(String account) {
		return StringUtils.isNumeric(account);
	}

	public static <T> T sendHttp(String httpUrl, String httpMethod, String params, Class<T> clazz) {

		try {
			StringBuffer buffer = new StringBuffer();
			sendHttp(httpUrl, httpMethod, params, buffer);

			return JSON.parseObject(buffer.toString(), clazz);
		} catch (NoSuchAlgorithmException | NoSuchProviderException | KeyManagementException | IOException e) {
			logger.error("", e);
		}
		return null;
	}

	public static JSONObject sendHttp(String httpUrl, String httpMethod, String params) {
		try {
			StringBuffer buffer = new StringBuffer();
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			sendHttp(httpUrl, httpMethod, params, buffer);
			return JSONObject.parseObject(buffer.toString());
		} catch (NoSuchAlgorithmException | NoSuchProviderException | KeyManagementException | IOException e) {
			logger.error("", e);
		}
		return null;
	}

	public static String sendHttp2(String httpUrl, String httpMethod, String params) {
		try {
			StringBuffer buffer = new StringBuffer();
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			sendHttp(httpUrl, httpMethod, params, buffer);
			return buffer.toString();
		} catch (NoSuchAlgorithmException | NoSuchProviderException | KeyManagementException | IOException e) {
			logger.error("", e);
		}
		return null;
	}

	private static void sendHttp(String httpUrl, String httpMethod, String params, StringBuffer resultBuffer)
			throws NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException, IOException {
		// 创建SSLContext对象，并使用我们指定的信任管理器初始化
		boolean sslSecure = httpUrl.toLowerCase().startsWith("https://");

		SSLSocketFactory ssf = null;

		URL url = new URL(httpUrl);
		HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
		if (sslSecure) {

			TrustManager[] tm = { new TrustedX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			ssf = sslContext.getSocketFactory();

			((HttpsURLConnection) httpUrlConn).setSSLSocketFactory(ssf);
		}

		httpUrlConn.setDoOutput(true);
		httpUrlConn.setDoInput(true);
		httpUrlConn.setUseCaches(false);
		// 设置请求方式（GET/POST）
		httpUrlConn.setRequestMethod(httpMethod);
		if ("GET".equalsIgnoreCase(httpMethod))
			httpUrlConn.connect();

		// 当有数据需要提交时
		if (null != params) {
			OutputStream outputStream = httpUrlConn.getOutputStream();
			// 注意编码格式，防止中文乱码
			outputStream.write(params.getBytes("UTF-8"));
			outputStream.close();
		}

		// 将返回的输入流转换成字符串
		InputStream inputStream = httpUrlConn.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

		String str = null;
		while ((str = bufferedReader.readLine()) != null) {
			resultBuffer.append(str);
		}
		bufferedReader.close();
		inputStreamReader.close();

		if (logger.isDebugEnabled()) {
			logger.debug("请求http[{}]收到信息：[{}]", url, resultBuffer.toString());
		}

		// 释放资源
		inputStream.close();
		inputStream = null;
		httpUrlConn.disconnect();
	}

	// 全角转半角的 转换函数
	public static final String full2HalfChange(String qjStr) throws UnsupportedEncodingException {
		StringBuffer outStrBuf = new StringBuffer("");
		String temStr = "";
		byte[] b = null;
		for (int i = 0; i < qjStr.length(); i++) {
			temStr = qjStr.substring(i, i + 1);
			// 全角空格转换成半角空格
			if (temStr.equals("　")) {
				outStrBuf.append(" ");
				continue;
			}
			b = temStr.getBytes("unicode");
			// 得到 unicode 字节数据

			if (b[2] == -1) {
				// 表示全角？
				b[3] = (byte) (b[3] + 32);
				b[2] = 0;
				outStrBuf.append(new String(b, "unicode"));
			} else {
				outStrBuf.append(temStr);
			}
		} // end for.
		return outStrBuf.toString();
	}

	public static String digestMD5(String str) {

		try {
			// 生成一个MD5加密计算摘要
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 计算md5函数
			md.update(str.getBytes("utf-8"));

			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			// 32位加密
			return buf.toString();

			// digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
			// BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
			// return new BigInteger(1, md.digest()).toString(16);//有问题，会少位数
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			logger.error("md5 digest err ", e);
		}
		return "";

	}
}
