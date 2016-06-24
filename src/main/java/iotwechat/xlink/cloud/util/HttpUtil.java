package iotwechat.xlink.cloud.util;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import common.Logger;

/**
 * 服务器端http请求工具类
 */
public class HttpUtil {
	
	private static Logger logger = Logger.getLogger(HttpUtil.class);
	
	/**
	 * 发起post请求
	 * <p>
	 * 替换ACCESS_TOKEN，访问凭证过期时，重新获取凭证并发起原有调用
	 */
	public static String doPost(String url, String body, String tokenId, String accessToken) throws HttpAccessTokenException {
		String realUrl = url.replace("ACCESS_TOKEN", accessToken);

		System.out.println(body);
		String rs = executePost(realUrl, body);
		JSONObject json = JSONObject.fromObject(rs);
		// 访问凭证失效时，重新进行一次获取凭证并发起原来业务调用
		if (json.containsKey("errcode")) {
			System.out.println("Error: " + json.toString());
			if (json.getInt("errcode") == 40001
					|| json.getInt("errcode") == 40014
					|| json.getInt("errcode") == 41001
					|| json.getInt("errcode") == 42001) {
				logger.error("Token error!");
				throw new HttpAccessTokenException();
			}
		}
		return rs;
	}

	public static String executeGetToke(String url) {
		String resultContent = null;
		try {
			HttpGet httpGet = new HttpGet(url);
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(httpGet);
			resultContent = new Utf8ResponseHandler().handleResponse(response);
			System.out.println("result of executeGetToke = " + resultContent);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultContent;
	}

	
	public static String executePost(String url, String body) {
		String resultContent = "";
		try {
			System.out.println(url);
			HttpPost httpPost = new HttpPost(url);
			StringEntity entity = new StringEntity(body, "UTF-8");
			httpPost.setEntity(entity);
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(httpPost);
			resultContent = new Utf8ResponseHandler().handleResponse(response);
			System.out.println("result of executePost = " + resultContent);
		} catch (Exception e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}
		return resultContent;
	}

	/**
	 * utf-8编码
	 */
	static class Utf8ResponseHandler implements ResponseHandler<String> {
		public String handleResponse(final HttpResponse response)
				throws HttpResponseException, IOException {
			final StatusLine statusLine = response.getStatusLine();
			final HttpEntity entity = response.getEntity();
			if (statusLine.getStatusCode() >= 300) {
				EntityUtils.consume(entity);
				throw new HttpResponseException(statusLine.getStatusCode(),
						statusLine.getReasonPhrase());
			}
			return entity == null ? null : EntityUtils
					.toString(entity, "UTF-8");
		}

	}

}
