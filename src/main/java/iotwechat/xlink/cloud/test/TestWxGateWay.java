package iotwechat.xlink.cloud.test;


import java.io.File;
import java.io.IOException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import iotwechat.xlink.cloud.util.ParseUtil;

public class TestWxGateWay {
//	private WxGateWayService wxGateWayService;
//	@Before
//	public void before(){
//		wxGateWayService=new WxGateWayServiceImpl();
//	}
	
	// @Test
	public void testWxgateWay() throws JsonProcessingException, IOException{
//		String devices="{\"tokenId\":\"wxaa14005b8431212\",\"appid\":\"wxe6ed495161ac50dc\",\"appsecret\":\"6cf4c228c844f7ea36b38fa738d00ba7\","
//				+ "\"pids\":[{\"pid\":2015032503},{\"pid\":\"CD15563D2503\"}]}";
		String devices="{\"tokenId\":\"c682fac2c9874585b42da11f7e1f7606\",\"appid\":\"wx209e3684cb550475\",\"appsecret\":\"5d9786d83d73eed5abc64057254b6ae3\","
				+ "\"pids\":[{\"pid\":dfe8fbe1118540c88699bc834af17431}]}";

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost("http://192.168.36.152/wxgateway/deviceAuth");
		StringEntity entity=new StringEntity(devices, "UTF-8");
		httpPost.setEntity(entity);
		HttpResponse response = httpClient.execute(httpPost);
		HttpEntity httpEntity = response.getEntity();
		System.out.println(ParseUtil.convertStreamToString(httpEntity.getContent()));
	}
	
	// @Test
	public void test(){
		String path="/root/wxgateway/application.properties";
		String path2="e:/properties/WxGateWay/application.properties";
		File file = new File(path);
		File file2 = new File(path2);
		System.out.println(path.substring(0, path.lastIndexOf("/")+1));
		System.out.println(file.exists());
		System.out.println(file2.exists());
	}
	
	// @Test
	public void testjson(){
		String str="{\"tokenId\":\"wxaa14005b8431212\",\"appid\":\"wxe6ed495161ac50dc\",\"appsecret\":\"6cf4c228c844f7ea36b38fa738d00ba7\","
				+ "\"devices\":[{\"deviceId\":101001134819,\"mac\":\"CD15563D0124\"},"
				+ "{\"deviceId\":1010314095234,\"mac\":\"CD15563D4762\"}]}";
		JSONObject obj = JSONObject.fromObject(str);
		System.out.println(obj.containsKey("tokenId"));
	}
	
	@Test
	public void testDeviceAuth() throws JsonProcessingException, IOException{
		JSONObject root = new JSONObject();
//		root.put("tokenId", "90a82c842a14494fa06e3c732a59b4fb");
		root.put("tokenId", "671d99a796af43dd9f616d5508326c02");
		root.put("appid", "wx577ad3df3de166e1");
		root.put("appsecret", "14daa0dab24b4b40ec310d45bc793cd3");
		JSONArray pids = new JSONArray();
		JSONObject pid = new JSONObject();
//		pid.put("pid", "e91d9e34215e4b15b7eec9a0a7835558");
		pid.put("pid", "745e12ae7ac14e78b00593c7ae501e3b");
		pids.add(pid);
		root.put("pids", pids);
		
		String request = root.toString();
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost("http://127.0.0.1:8090/wxgateway/deviceAuth");
		StringEntity entity=new StringEntity(request, "UTF-8");
		httpPost.setEntity(entity);
		HttpResponse response = httpClient.execute(httpPost);
		HttpEntity httpEntity = response.getEntity();
		System.out.println(ParseUtil.convertStreamToString(httpEntity.getContent()));
	}
}
