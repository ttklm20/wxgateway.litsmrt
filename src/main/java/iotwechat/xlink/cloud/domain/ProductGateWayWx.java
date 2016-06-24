package iotwechat.xlink.cloud.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 产品网关表
 * @author LDXN
 */
@Entity
public class ProductGateWayWx implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static class closeStrategyType{
		public static final String close1 = "1";
		public static final String close2 = "2";
		public static final String close3 = "3";
		public static final String [] name = {close1, close2,close3};
		public static final String [] val = {"退出公众号页面时即断开连接", "退出公众号之后保持连接不断开","退出公众号之后一直保持连接（设备主动断开连接后，微信尝试重连）"};
	}
	public static class connStrategyType{
		public static final String conn1 = "1";
		public static final String conn4 = "4";
		public static final String conn8 = "8";
		public static final String [] name = {conn1, conn4,conn8};
		public static final String [] val = {"在公众号对话页面，不停的尝试连接设备", "处于非公众号页面（如主界面等），微信自动连接","进入微信后即刻开始连接"};
	}
	public static class cryptMethodType{
		public static final String crypt0 = "0";
		public static final String crypt1 = "1";
		public static final String [] name = {crypt0, crypt1};
		public static final String [] val = {"不加密 ", "AES加密（CBC模式，PKCS7填充方式）"};
	}
	public static class authVerType{
		public static final String authV0 = "0";
		public static final String authV1 = "1";
		public static final String [] name = {authV0, authV1};
		public static final String [] val = {"不加密的version", "version 1"};
	}
	public static class manuMacPosType{
		public static final String manu_1 = "-1";
		public static final String manu_2 = "-2";
		public static final String [] name = {manu_1, manu_2};
		public static final String [] val = {"在尾部", "表示不包含mac地址"};
	}
	public static class serMacPosType{
		public static final String ser_1 = "-1";
		public static final String ser_2 = "-2";
		public static final String [] name = {ser_1, ser_2};
		public static final String [] val = {"在尾部", "表示不包含mac地址"};
	}
	public static class opTypeVal{
		public static final String op0 = "0";
		public static final String op1 = "1";
		public static final String [] name = {op0, op1};
		public static final String [] val = {"设备授权（缺省值为0） ", "设备更新（更新已授权设备的各属性值）"};
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;		//产品网关ID
	@Column
	private Long gateWayWxId;			//该产品对应的微信网关的配置信息ID
	@Column
	private String appName;//公众号的AppName
	
	@Column(length=32)
	private String productId;		//产品ID
	@Column
	private String pName;		//产品名称
	@Column(length=32)
	private String tokenId;		
	////////////////////////////////////BEGIN 以下的字段含义请参照上述对应的Type类
	@Column(length=32)
	private String authKey;//auth及通信的加密key，第三方需要将key烧制在设备上（128bit），格式采用16进制串的方式（长度为32字节），不需要0X前缀，如： 1234567890ABCDEF1234567890ABCDEF
	
	@Column(length=10)
	private String connectProtocol;//连接协议
	
	@Column(length=3,nullable=false)
	private String closeStrategy=closeStrategyType.close1;//断开策略，
	
	@Column(length=3,nullable=false)
	private String connStrategy=connStrategyType.conn1;//连接策略，
	
	@Column(length=3,nullable=false)
	private String cryptMethod=cryptMethodType.crypt0;//auth加密方法，
	
	@Column(length=3,nullable=false)
	private String authVer=authVerType.authV0;//auth version，设备和微信进行auth时，会根据该版本号来确认auth buf和auth key的格式
	
	@Column(length=3,nullable=false)
	private String manuMacPos=manuMacPosType.manu_1;//表示mac地址在厂商广播manufature data里含有mac地址的偏移，
	
	@Column(length=3,nullable=false)
	private String serMacPos=serMacPosType.ser_1;//表示mac地址在厂商serial number里含有mac地址的偏移，
	////////////////////////////////////END

	public Long getId() {
		return id;
	}
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getGateWayWxId() {
		return gateWayWxId;
	}
	public void setGateWayWxId(Long gateWayWxId) {
		this.gateWayWxId = gateWayWxId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getpName() {
		return pName;
	}
	public void setpName(String pName) {
		this.pName = pName;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getAuthKey() {
		return authKey;
	}
	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}
	public String getConnectProtocol() {
		return connectProtocol;
	}
	public void setConnectProtocol(String connectProtocol) {
		this.connectProtocol = connectProtocol;
	}
	public String getCloseStrategy() {
		return closeStrategy;
	}
	public void setCloseStrategy(String closeStrategy) {
		this.closeStrategy = closeStrategy;
	}
	public String getConnStrategy() {
		return connStrategy;
	}
	public void setConnStrategy(String connStrategy) {
		this.connStrategy = connStrategy;
	}
	public String getCryptMethod() {
		return cryptMethod;
	}
	public void setCryptMethod(String cryptMethod) {
		this.cryptMethod = cryptMethod;
	}
	public String getAuthVer() {
		return authVer;
	}
	public void setAuthVer(String authVer) {
		this.authVer = authVer;
	}
	public String getManuMacPos() {
		return manuMacPos;
	}
	public void setManuMacPos(String manuMacPos) {
		this.manuMacPos = manuMacPos;
	}
	public String getSerMacPos() {
		return serMacPos;
	}
	public void setSerMacPos(String serMacPos) {
		this.serMacPos = serMacPos;
	}
}
