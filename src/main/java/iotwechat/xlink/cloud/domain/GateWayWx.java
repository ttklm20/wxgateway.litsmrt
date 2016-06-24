package iotwechat.xlink.cloud.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class GateWayWx implements Serializable {
	/**
	 * 信息加密方式
	 */
	public static class encodeType{
		public static final String plaintext = "100";
		public static final String compatible = "200";
		public static final String security = "300";
		public static final String [] name = {plaintext, compatible,security};
		public static final String [] val = {"明文模式", "兼容模式","安全模式"};
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;			//微信网关表ID
	
	@Column
	private String appID;//公众号的APPID
	
	@Column
	private String appName;//公众号的AppName
	
	@Column
	private String appsecret;//公众号对应的appsecret
	
	@Column(length=3)
	private String encode=encodeType.plaintext;
	
	@Column(length=32)
	private String encodingAESKey;
	
	@Column
	private String URL;//接口配置信息
	
	@Column
	private String token;//验证信息
	
	@Column
	private String domName;//配置域名
	
	@Column
	private Long corpId;//公司ID
	
	@Column
	private String tokenId;//每个公众号维护一个tokenID用来表示access_token

	@Column
	private String qrImgDir;//当前公众号的设备二维码图片存放的路径
	
	public String getEncodingAESKey() {
		return encodingAESKey;
	}

	public void setEncodingAESKey(String encodingAESKey) {
		this.encodingAESKey = encodingAESKey;
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAppID() {
		return appID;
	}

	public void setAppID(String appID) {
		this.appID = appID;
	}

	public String getAppsecret() {
		return appsecret;
	}

	public void setAppsecret(String appsecret) {
		this.appsecret = appsecret;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getDomName() {
		return domName;
	}

	public void setDomName(String domName) {
		this.domName = domName;
	}

	public Long getCorpId() {
		return corpId;
	}

	public void setCorpId(Long corpId) {
		this.corpId = corpId;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getQrImgDir() {
		return qrImgDir;
	}

	public void setQrImgDir(String qrImgDir) {
		this.qrImgDir = qrImgDir;
	}
}
