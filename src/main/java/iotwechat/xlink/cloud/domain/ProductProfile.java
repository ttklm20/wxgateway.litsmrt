package iotwechat.xlink.cloud.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * 产品明细数据，保存一些动态的数据
 * @author chenhaizhen
 *
 */
@Entity
public class ProductProfile {
	
	@Id
	@Column(length=32,nullable=false)	
	private String id;					//产品ID
	@Column
	private int deviceCount = 0;		//当前产品下，导入设备的总量

	@Column
	private int activatedCount = 0;		//当前产品下，已经激活设备的数量

 	@Column
	private long onLineNum;             //当前在线人数
	@Column
	private long totalOnLineNum;        //累计上线数
	@Column
	private long unDealFailureNum; 	   //未处理故障数
	@Column
	private long totalDealFailureNum;   //总的故障数
	@Column
	private long totoalActiveCodeNum;   //该产品总共生成的激活码的数量
	@OneToOne
	@JoinColumn(name = "productId")
	private Product product;
 	public ProductProfile() {

	}
 	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getDeviceCount() {
		return deviceCount;
	}
	public void setDeviceCount(int deviceCount) {
		this.deviceCount = deviceCount;
	}
	public int getActivatedCount() {
		return activatedCount;
	}
	public void setActivatedCount(int activatedCount) {
		this.activatedCount = activatedCount;
	}
	public long getOnLineNum() {
		return onLineNum;
	}
	public void setOnLineNum(long onLineNum) {
		this.onLineNum = onLineNum;
	}
	public long getTotalOnLineNum() {
		return totalOnLineNum;
	}
	public void setTotalOnLineNum(long totalOnLineNum) {
		this.totalOnLineNum = totalOnLineNum;
	}
	public long getUnDealFailureNum() {
		return unDealFailureNum;
	}
	public void setUnDealFailureNum(long unDealFailureNum) {
		this.unDealFailureNum = unDealFailureNum;
	}
	public long getTotalDealFailureNum() {
		return totalDealFailureNum;
	}
	public void setTotalDealFailureNum(long totalDealFailureNum) {
		this.totalDealFailureNum = totalDealFailureNum;
	}
	public long getTotoalActiveCodeNum() {
		return totoalActiveCodeNum;
	}
	public void setTotoalActiveCodeNum(long totoalActiveCodeNum) {
		this.totoalActiveCodeNum = totoalActiveCodeNum;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	 
	 
	
}
