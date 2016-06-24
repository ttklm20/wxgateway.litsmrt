package iotwechat.xlink.cloud.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iotwechat.xlink.cloud.config.WeChatSettings;
import iotwechat.xlink.cloud.consts.WxConfig;
import iotwechat.xlink.cloud.domain.BoundInfo;
import iotwechat.xlink.cloud.domain.GateWayWx;
import iotwechat.xlink.cloud.redis.RedisBase;
import iotwechat.xlink.cloud.repository.WxGateWayRepository;
import iotwechat.xlink.cloud.service.BoundInfoService;
import iotwechat.xlink.cloud.service.CallbackService;
import iotwechat.xlink.cloud.util.HttpCilentUtil;
import iotwechat.xlink.cloud.util.SHA1;
import iotwechat.xlink.cloud.util.ZipFile;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.ClientProtocolException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.JsonNode;
import common.Logger;



@Controller
public class CallbackController {
	@Inject 
	private CallbackService callbackService;
	@Inject
	private WxGateWayRepository wxGateWayRepository;
	@Inject
	private BoundInfoService boundInfoService;
	@Inject
	private RedisBase redisBase;
	@Autowired
	private WeChatSettings weChatSettings;
	
	private Logger logger = Logger.getLogger(CallbackController.class); 
	/**
	 * 内部测试用的iot认证
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value="/test/iotxlink",method=RequestMethod.GET)
	protected void wxTokenCheck(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			// 开发者接入验证
			String timestamp = req.getParameter("timestamp");
			String nonce = req.getParameter("nonce");
			String signature = req.getParameter("signature");
			String echostr = req.getParameter("echostr");
			String user=req.getParameter("user");
			
			logger.info(req.toString());
			
			boolean authorized = false;
			// TODO: 根据tokenId或者user，从数据库拿取相关配置，然后进行验证
			// ...
			authorized = true;
			
			if( authorized ) {
				// 验证成功
				out(echostr, resp);				
			} else {
				out("", resp);
			}

		} catch (Throwable e) {
			e.printStackTrace();
			out("", resp);
		}
	}
	
	/**
	 * 微信公众平台验证URL地址接口，如：http：//42.121.122.228/iotxlink
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value="/wxgateway",method=RequestMethod.GET)
	protected void checkURL(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			// 开发者接入验证
			String timestamp = req.getParameter("timestamp");
			String nonce = req.getParameter("nonce");
			String signature = req.getParameter("signature");
			String echostr = req.getParameter("echostr");

			if (signature.equals(SHA1.gen(WxConfig.TOKEN, timestamp, nonce))) {
				out(echostr, resp);
			} else {
				out("", resp);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			out("", resp);
		}
	}
	/**
	 * 微信服务器与第三方厂商服务器通信的入口
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	@RequestMapping(value="/wxgateway",method=RequestMethod.POST)
	protected void open(HttpServletRequest req,HttpServletResponse resp) throws IOException{
		try {
			// 编码格式
			req.setCharacterEncoding("UTF-8");
			System.out.println("hello,iotxlink!");
			Map<String, String[]> paramMap = req.getParameterMap();
			for(String key: paramMap.keySet()){
				System.out.println(key+"："+paramMap.get(key));
			}
			// 验证签名
			String timestamp = req.getParameter("timestamp");
			String nonce = req.getParameter("nonce");
			String signature = req.getParameter("signature");
			if (!signature.equals(SHA1.gen(WxConfig.TOKEN, timestamp, nonce))) {
				out("", resp);
				return;
			}

			// 解析xml
			Map<String, String> reqMap = parseXml(req.getInputStream());
			System.out.println("reqMap=" + reqMap);

			// 处理请求
			String xmlStr = callbackService.handle(reqMap);

			System.out.println("xmlStr=" + xmlStr);

			// null 转为空字符串
			xmlStr = xmlStr == null ? "" : xmlStr;

			out(xmlStr, resp);
		} catch (Throwable e) {
			e.printStackTrace();
			// 异常时响应空串
			out("", resp);
		}
	}

	/**
	 * 输出字符串
	 */
	protected void out(String str, HttpServletResponse response) {
		Writer out = null;
		try {
			response.setContentType("text/xml;charset=UTF-8");
			out = response.getWriter();
			out.append(str);
			out.flush();
		} catch (IOException e) {
			// ignore
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}

	/**
	 * 解析请求中的xml元素为Map
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, String> parseXml(InputStream in)
			throws DocumentException, IOException {
		Map<String, String> map = new HashMap<String, String>();
		SAXReader reader = new SAXReader();
		Document document = reader.read(in);
		Element root = document.getRootElement();
		List<Element> elementList = root.elements();
		for (Element e : elementList) {
			map.put(e.getName(), e.getText());
		}
		return map;
	}
}
