package iotwechat.xlink.cloud.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

 

import iotwechat.xlink.cloud.domain.BoundInfo;
import iotwechat.xlink.cloud.domain.Device;
import iotwechat.xlink.cloud.domain.ProductGateWayWx;
import iotwechat.xlink.cloud.redis.RedisBase;
import iotwechat.xlink.cloud.repository.BoundInfoRepository;
import iotwechat.xlink.cloud.repository.DeviceRepository;
import iotwechat.xlink.cloud.repository.ProductWxGwRepository;
import iotwechat.xlink.cloud.util.Base64;
import iotwechat.xlink.cloud.util.MD5Util;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

@Service
@Validated
public class BoundInfoServiceImpl implements BoundInfoService{
	@Inject
	private BoundInfoRepository boundInfoRepository;
	@Inject
	private DeviceRepository deviceRepository;
	@Inject
	private ProductWxGwRepository productWxGwRepository;
	@Inject
	private RedisBase redisBase;
	/**
	 * 保存扫描绑定的设备和用户信息
	 */
	@Override
	public void saveBoundInfo(Map<String, String> reqMap) {
		//获取设备的信息
		Device device = deviceRepository.findById(Long.parseLong(reqMap.get("DeviceID")));
		System.out.println("device:"+device);
		//查找产品和公众号的关联信息
		ProductGateWayWx pgw=productWxGwRepository.findByProductIdAndAppName(device.getPid(),reqMap.get("DeviceType"));
		System.out.println("pgw:"+pgw);
//		removeBoundInfo(reqMap.get("FromUserName"), reqMap.get("DeviceType"));
		//当产品和公众号的关系绑定时候才真正绑定设备和用户和公众号的关系
		if (pgw!=null) {
			BoundInfo hasBoundInfo = boundInfoRepository.findByDeviceTypeAndFromUserNameAndDeviceID(reqMap.get("DeviceType"),reqMap.get("FromUserName"), reqMap.get("DeviceID"));
			if (hasBoundInfo==null) {
				BoundInfo boundInfo = new BoundInfo();
				boundInfo.setFromUserName(reqMap.get("FromUserName"));
				boundInfo.setDeviceID(reqMap.get("DeviceID"));
				boundInfo.setOpenID(reqMap.get("OpenID"));
				boundInfo.setDeviceType(reqMap.get("DeviceType"));
				System.out.println(reqMap.get("Content"));
				//"V0lGSQ=="是经过base64编码的"WIFI"特征字符串
				
				if ("V0lGSQ==".equals(reqMap.get("Content"))) {
					boundInfo.setType(BoundInfo.Type.wifi);
				}else {
					boundInfo.setType(BoundInfo.Type.blueTooth);
				}
				boundInfoRepository.save(boundInfo);
				redisBase.del(boundInfo.getDeviceType()+boundInfo.getFromUserName());
				System.out.println(boundInfo.toString());
			}else if (BoundInfo.StatusVal.unvalid.equals(hasBoundInfo.getStatus())) {
				hasBoundInfo.setStatus(BoundInfo.StatusVal.isvalid);
				boundInfoRepository.save(hasBoundInfo);
				redisBase.del(hasBoundInfo.getDeviceType()+hasBoundInfo.getFromUserName());
				System.out.println(hasBoundInfo.toString());
			}
			//DeviceTypeAndFromUserName
			//redisBase.del(key);
		}
	}
	@Override
	public void removeBoundInfo(Map<String, String> reqMap) {
		Device device = deviceRepository.findById(Long.parseLong(reqMap.get("DeviceID")));
		
		BoundInfo boundInfo=boundInfoRepository.findByDeviceTypeAndDeviceID(reqMap.get("DeviceType"), Long.parseLong(reqMap.get("DeviceID")));
		boundInfo.setStatus(BoundInfo.StatusVal.unvalid);
		boundInfoRepository.save(boundInfo);
		System.out.println(boundInfo);
	}
	@Deprecated
	@Override
	public BoundInfo findByFromUserAndStatus(String fromUser,String status) {
		
		return boundInfoRepository.findByFromUserNameAndStatus(fromUser,BoundInfo.StatusVal.isvalid);
	}
	@Deprecated
	@Override
	public BoundInfo findByFromUserAndType(String fromUser, String wifi) {
		
		return boundInfoRepository.findByFromUserNameAndType(fromUser,wifi);
	}
	@Deprecated
	@Override
	public List<BoundInfo> findByDeviceTypeAndFromUser(String deviceType, String fromUser) {
		
		return boundInfoRepository.findByDeviceTypeAndFromUserName(deviceType, fromUser);
	}
	/**
	 * 查找用户和公众号绑定的设备列表
	 */
	@Override
	public List<BoundInfo> findByDeviceTypeAndFromUserNameOrderById(String appName,
			String openId) {
		// TODO Auto-generated method stub
		return boundInfoRepository.findByDeviceTypeAndFromUserNameOrderByIdDesc(appName,openId);
	}
	/**
	 * 删除用户和公众号之间所有的绑定设备
	 */
	@Override
	public void removeBoundInfo(String fromUser, String deviceType) {
		List<BoundInfo> list=boundInfoRepository.findByDeviceTypeAndFromUserName(deviceType, fromUser);
		boundInfoRepository.delete(list);
	}
	/**
	 * 根据deviceId获取签名消息
	 */
	@Override
	public Map<String, String> getCmServerByDeviceId(Long deviceId) {
		String key="device-cm-"+deviceId.toString();
		String cmidString=redisBase.getHashSetValue(key, "id");
		key="cm-status-"+cmidString;
	    return redisBase.hashGetAll(key);
	}
}
