package iotwechat.xlink.cloud.test;

import iotwechat.xlink.cloud.util.HttpUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;

public class DeviceAuthTest {
	
	/**
	 * 测试授权设备列表
	 */

	public void deviceAuth() {
		JSONObject request = new JSONObject();
		request.put("tokenId", "19cea333b0a44464ba0430648de5fb7b");
		request.put("appid", "wx209e3684cb550475");
		request.put("appsecret", "5d9786d83d73eed5abc64057254b6ae3");
		JSONArray pids = new JSONArray();
		JSONObject pid = new JSONObject();
		pid.put("pid", "745e12ae7ac14e78b00593c7ae501e3b");
		pids.add(pid);
		request.put("pids", pids);
		
		String body = request.toString();
		String url = "http://127.0.0.1:8090/wxgateway/deviceAuth";
		HttpUtil.executePost(url, body);
	}
	
	@Test
	public void testGetSignature() {
		JSONObject request = new JSONObject();
		request.put("tokenId", "19cea333b0a44464ba0430648de5fb7b");
		request.put("noncestr", "Wm3WZYTPz0wzccnW");
		request.put("timestamp", "1414587457");
		request.put("url", "http://wx3.xlink.cn/index.html");
		
		String body = request.toString();
		String url = "http://127.0.0.1:8090/wxgateway/urlSignature";
		HttpUtil.executePost(url, body);
	}

}
