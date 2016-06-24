package iotwechat.xlink.cloud.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "redis")
public class RedisConSettings {

	private String host;
	private int port;
	private String pass;
	private int defaultdb;
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public int getDefaultdb() {
		return defaultdb;
	}
	public void setDefaultdb(int defaultdb) {
		this.defaultdb = defaultdb;
	}
}
