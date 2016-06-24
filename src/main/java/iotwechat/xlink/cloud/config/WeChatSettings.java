package iotwechat.xlink.cloud.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "wechat.iot")
public class WeChatSettings {
	private String address;
	private String mainpage;
	private String token; //反相认证的token
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMainpage() {
		return mainpage;
	}
	public void setMainpage(String mainpage) {
		this.mainpage = mainpage;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
 
	
	
}
