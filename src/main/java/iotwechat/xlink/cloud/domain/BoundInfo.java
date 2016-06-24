package iotwechat.xlink.cloud.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BoundInfo {
	public static class StatusVal{
		public static final String isvalid = "100";
		public static final String unvalid = "200";
		public static final String [] name = {isvalid, unvalid};
		public static final String [] val = {"绑定", "未绑定"};
	}
	public static class Type{
		public static final String blueTooth = "100";
		public static final String wifi = "200";
		public static final String [] name = {blueTooth, wifi};
		public static final String [] val = {"蓝牙", "wifi设备"};
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;//绑定关系的ID
	@Column
	private String fromUserName;//绑定用户
	//绑定设备ID，由于微信官方产生的设备ID为string类型，所以前期设置ID类型为string
	//xlink的设备类型为long型，故在必要时候需要做转换
	@Column
	private String deviceID;
	@Column
	private String openID;//用户的ID
	@Column
	private String deviceType;//设备的公众号
	@Column
	private String status=StatusVal.isvalid;//绑定用户是否有效
	@Column
	private String type;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFromUserName() {
		return fromUserName;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public String getOpenID() {
		return openID;
	}
	public void setOpenID(String openID) {
		this.openID = openID;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
