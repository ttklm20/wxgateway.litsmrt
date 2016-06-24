package iotwechat.xlink.cloud.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * 产品实体类
 * @author chenhaizhen
 *
 */
@Entity
public class Product {
	
	/**
	 * 产品类型 与 Product中type的对应
	 */
	public static class TypeVal{
		public static final int wifi = 1;
		public static final int zigBee = 2;
		public static final int Bluetooth = 3;
		public static final int [] name = {wifi, zigBee,Bluetooth};
		public static final String [] typeVal ={"Wifi设备", "Zigbee网关","蓝牙设备"};
	}
	public static class StatusVal{
		public static final String normal = "100";
		public static final String delete = "200";
		public static final String [] name = {normal, delete};
		public static final String [] val = {"正常", "已删除"};
	}
	

	@Id
	@Column(length=32,nullable=false)	
	private String id;					//产品ID,32个随机字符串

	@Column(length = 64, nullable = false)
	private String name;				//产品名称

	@Column(length = 255, nullable = true, columnDefinition = "TEXT")
	private String description;			//产品描述信息

	@Column(length = 32, nullable = false)
	private String secret;				//产品秘钥匙32个随机字符串
	@Column
	private int type;					//产品类型，分为"Wifi设备"与"Zigbee网关"两种设备类型
	@Column(nullable = false)
	private boolean privacy = true;		//产品权限，true代表公有，false代表私有
	@Column
	private Long creator;			//该产品的创建者，与当前登录用户的ID相关联<account实体类的ID>
	@Column
	private Date createdDate=new Date(); //创建当前产品的时间
	@Column
	private Long corpId;				//公司ID,关联Corp主键ID
	@Column
	private int currentVersion;		   //当前固件最新版本号
	@OneToOne(mappedBy="product" ,targetEntity=ProductProfile.class,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private ProductProfile productProfile;
	@Column(length=3)
	private String status=StatusVal.normal;

	public Product() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

 

	public boolean isPrivacy() {
		return privacy;
	}

	public void setPrivacy(boolean privacy) {
		this.privacy = privacy;
	}

	public long getCreator() {
		return creator;
	}

	public void setCreator(long creator) {
		this.creator = creator;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getCorpId() {
		return corpId;
	}

	public void setCorpId(Long corpId) {
		this.corpId = corpId;
	}
	
	
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setCreator(Long creator) {
		this.creator = creator;
	}

	public int getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(int currentVersion) {
		this.currentVersion = currentVersion;
	}

	public ProductProfile getProductProfile() {
		return productProfile;
	}

	public void setProductProfile(ProductProfile productProfile) {
		this.productProfile = productProfile;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
