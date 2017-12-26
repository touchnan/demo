package cn.touch.demo.weixin;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.helpers.DefaultHandler;

import cn.touch.demo.util.Constants;

public class WeixinServlet extends HttpServlet {
	private final static Logger logger = LoggerFactory.getLogger(WeixinServlet.class);

	protected String sign4UnifiedOrder(Map<String, String> paraMap, String signKey) {
		// 排序，签字
		String[] keys = paraMap.keySet().toArray(new String[paraMap.size()]);
		Arrays.sort(keys);
		StringBuffer psf = new StringBuffer();
		for (int i = 0; i < keys.length; i++) {
			if (i != 0) {
				psf.append("&");
			}
			psf.append(keys[i]).append("=").append(paraMap.get(keys[i]));
		}
		if (logger.isDebugEnabled()) {
			logger.debug("生成字符串：{}", psf.toString());
		}
		psf.append("&key=").append(signKey);
		if (logger.isDebugEnabled()) {
			logger.debug("连接商户key：{}", psf.toString());
		}
		return Constants.digestMD5(psf.toString()).toUpperCase();
	}

	protected Map<String, String> doParseXml(String retXml) {
		Map<String, String> map = new HashMap<>();
		SAXParserFactory saxfac = SAXParserFactory.newInstance();
		try {
			// 不允许DTDs (doctypes) ,几乎可以阻止所有的XML实体攻击
			String FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
			saxfac.setFeature(FEATURE, true);
		} catch (ParserConfigurationException | SAXNotRecognizedException | SAXNotSupportedException e) {
			logger.error("xml parse disallow-doctype err ", e);
		}

		try {
			SAXParser saxparser = saxfac.newSAXParser();
			InputStream is = new ByteArrayInputStream(retXml.getBytes("UTF-8"));

			saxparser.parse(is, new DefaultHandler() {
				private String name;

				@Override
				public void startDocument() throws SAXException {
				}

				@Override
				public void endDocument() throws SAXException {
				}

				@Override
				public void startElement(String uri, String localName, String qName, Attributes attributes)
						throws SAXException {
					this.name = qName;
				}

				@Override
				public void endElement(String uri, String localName, String qName) throws SAXException {
				}

				@Override
				public void characters(char[] ch, int start, int length) throws SAXException {
					map.put(this.name, new String(ch, start, length));
				}
			});
		} catch (ParserConfigurationException | SAXException | IOException e) {
			logger.error("xml parse err ", e);
		}
		return map;

	}
	
	protected String xml2UnifiedOrder(Map<String, String> paraMap) {
		StringBuffer sbf = new StringBuffer("<xml>");
		for (Map.Entry<String, String> entry : paraMap.entrySet()) {
			sbf.append("<").append(entry.getKey()).append("><![CDATA[").append(entry.getValue()).append("]]></")
					.append(entry.getKey()).append(">");
		}
		sbf.append("</xml>");
		return sbf.toString();
	}
}
