package iotwechat.xlink.cloud.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 设备实体类,与关系数据库中的device表对应
 * @author chenhaizhen
 *
 */
@Entity
public class Device  implements Serializable {
	public static class StatusVal{
		public static final String normal = "100";
		public static final String delete = "200";
		public static final String [] name = {normal, delete};
		public static final String [] val = {"正常", "已删除"};
	}
	public static class onlineVal{
		public static final String online = "100";
		public static final String offline = "200";
		public static final String [] name = {online, offline};
		public static final String [] val = {"在线", "离线"};
	}
	
	private static final long serialVersionUID = 1L;
	
	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;					//设备ID
	
	@Column(length = 32)
	private String sn;					//设备编号,须唯一
	
	@Column(length = 32)
	private String activationCode;		//设备激活码,产品密钥与SN的hash值  V1版本
	
	@Column
	private boolean activated=false;			//设备的激活状态,true代表已激活,false代表未激活 
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date activatedDate;			//设备激活时间
	
	@Column
    private String description;			//设备描述
	
	@Temporal(TemporalType.TIMESTAMP)
    private Date createdDate=new Date();			//设备的创建时间	
	
	@Column(nullable = false)
	private Long creator;			//该设备的创建者,与当前登录用户的ID相关联<account实体类的ID>
	
	@Column(nullable = false)
	private String pid;				//产品ID,关联Product主键ID
	
	@Column
	private Long corpId;			//公司ID,关联Corp主键ID
	
	@Column 
	private String type;				//设备类型
	
	@Column(length = 32)
	private String apiKey;
	
	@Column(length=30)
	private String macAddress;			//设备的MAC地址
	@Column
	private int totalOnLineNum;        //累计上线数
	
	@Column
	private Boolean onLine;			//是否在线，false为否，true为是
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLoginTime;			//最后一次上线时间
 
	@Column(length = 5)
	private String wifiHardVersion;		//wifi 硬件版本
	@Column(length = 5)
	private String wifiFirmwareVersion;      //wifi 软件版本
	@Column(length = 5)
	private String mcuHardVersion;		//mcu 硬件版本
	@Column(length = 5)   
	private String mcuFirmwareVersion;      //mcu 软件版本
	@Column(length = 3) 
	private String status=StatusVal.normal; //如果删除则为未激活状态
	@Column(length=1000)
	private String wxTokenIds;			//接入的公众号集合
	
	public Device() {
		
	}
	
    public Device(String sn, String description) {
    	super();
    	
        this.sn = sn;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }


	public String getSn() {
		return sn;
	}


	public void setSn(String sn) {
		this.sn = sn;
	}


	public void setId(long id) {
		this.id = id;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getActivationCode() {
		return activationCode;
	}


	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}	
	
	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public Long getCreator() {
		return creator;
	}


	public Date getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}


	public void setCreator(Long creator) {
		this.creator = creator;
	}
	
	
	public Date getActivatedDate() {
		return activatedDate;
	}


	public void setActivatedDate(Date activatedDate) {
		this.activatedDate = activatedDate;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public Long getCorpId() {
		return corpId;
	}

	public void setCorpId(Long corpId) {
		this.corpId = corpId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public int getTotalOnLineNum() {
		return totalOnLineNum;
	}

	public void setTotalOnLineNum(int totalOnLineNum) {
		this.totalOnLineNum = totalOnLineNum;
	}

	public Boolean getOnLine() {
		return onLine;
	}

	public void setOnLine(Boolean onLine) {
		this.onLine = onLine;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getWifiHardVersion() {
		return wifiHardVersion;
	}

	public void setWifiHardVersion(String wifiHardVersion) {
		this.wifiHardVersion = wifiHardVersion;
	}

 

	public String getMcuHardVersion() {
		return mcuHardVersion;
	}

	public void setMcuHardVersion(String mcuHardVersion) {
		this.mcuHardVersion = mcuHardVersion;
	}
 

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getWifiFirmwareVersion() {
		return wifiFirmwareVersion;
	}

	public void setWifiFirmwareVersion(String wifiFirmwareVersion) {
		this.wifiFirmwareVersion = wifiFirmwareVersion;
	}

	public String getMcuFirmwareVersion() {
		return mcuFirmwareVersion;
	}

	public void setMcuFirmwareVersion(String mcuFirmwareVersion) {
		this.mcuFirmwareVersion = mcuFirmwareVersion;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getWxTokenIds() {
		return wxTokenIds;
	}

	public void setWxTokenIds(String wxTokenIds) {
		this.wxTokenIds = wxTokenIds;
	}
}
