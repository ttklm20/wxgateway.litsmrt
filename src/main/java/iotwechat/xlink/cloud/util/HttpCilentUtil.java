package iotwechat.xlink.cloud.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpCilentUtil {
	 
	// private static DefaultHttpClient httpClient = new DefaultHttpClient();
	/**
	 * 
	 * @param data 发送的数据
	 * @param url  请求地址
	 * @return
	 * @throws ClientProtocolException 
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static String excuteStringEntity(String data,String url) throws ClientProtocolException, IOException  {
	 DefaultHttpClient httpClient = new DefaultHttpClient();
		//httpClient.getConnectionManager().new;
		HttpPost httpPost = new HttpPost(url);
		StringEntity entity=new StringEntity(data, "UTF-8");
		httpPost.setEntity(entity);
		
		HttpResponse response = httpClient.execute(httpPost);
		HttpEntity httpEntity = response.getEntity();
		String result=convertStreamToString(httpEntity.getContent());
		EntityUtils.consume(httpEntity);
		httpPost.abort();
		httpClient.getConnectionManager().shutdown();
		//httpPost.releaseConnection();
		return  result;
	} 
	public static String excuteStringEntityGet(String url) throws ClientProtocolException, IOException  {
		 DefaultHttpClient httpClient = new DefaultHttpClient();
			//httpClient.getConnectionManager().new;
			HttpGet httpGet = new HttpGet(url);
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity httpEntity = response.getEntity();
			String result=convertStreamToString(httpEntity.getContent());
			EntityUtils.consume(httpEntity);
			httpGet.abort();
			httpClient.getConnectionManager().shutdown();
			//httpPost.releaseConnection();
			return  result;
		} 
	/**
	 * 流转换为字符串
	 * @param is
	 * @return
	 */
	private  static String convertStreamToString(InputStream is) {      
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));      
        StringBuilder sb = new StringBuilder();      
        String line = null;
       try {      
           while ((line = reader.readLine()) != null) {      
                sb.append(line + "\n");      
            }      
        } catch (IOException e) {      
            e.printStackTrace();      
        } finally {      
           try {      
                is.close();      
            } catch (IOException e) {      
                e.printStackTrace();      
            }      
        }      
       return sb.toString();      
    }
	public static JsonNode parseBody(String body) throws JsonProcessingException, IOException{
		
		  ObjectMapper mapper = new ObjectMapper();  
		  JsonNode rootNode=null;
		  HashMap<Object, Object> returnMap = new HashMap<Object, Object>();
		rootNode = mapper.readTree(body);
		return rootNode;
	}
}
