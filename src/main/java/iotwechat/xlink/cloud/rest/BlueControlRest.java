package iotwechat.xlink.cloud.rest;

import java.security.NoSuchAlgorithmException;

import iotwechat.xlink.cloud.api.MpApi;
import iotwechat.xlink.cloud.domain.AccessToken;
import iotwechat.xlink.cloud.util.AccessTokenHelper;
import iotwechat.xlink.cloud.util.SHA1;
import iotwechat.xlink.cloud.util.WeChatV1;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlueControlRest {
	@Inject
	private AccessTokenHelper accessTokenHelper;

	/**
	 * 在HTML页面控制蓝牙设备，首先要通过后台获取签名返回页面进行wx.config签名认证。
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getSignature", method = RequestMethod.POST)
	public String getTokenAccess(String tokenId) {
		// 通过token取拿到js_ticket
		String jsapi_ticket = accessTokenHelper
				.getJSAPITicketWithTokenId(tokenId);
		String noncestr = "Wm3WZYTPz0wzccnW"; // 任意随机
		String timestamp = "1414587457";
		String url = "http://wx3.xlink.cn/deviceService/configWxDeviceWifi.html";
		String abcString = "jsapi_ticket=" + jsapi_ticket + "&"
				+ "noncestr=" + noncestr + "&" + "timestamp=" + timestamp + "&"
				+ "url=" + url;

		System.out.println(abcString);

		String qm = "";
		try {
			qm = SHA1.gen(abcString);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return qm;
	}
}
