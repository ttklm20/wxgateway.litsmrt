package iotwechat.xlink.cloud.service;

import iotwechat.xlink.cloud.domain.BoundInfo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

public interface BoundInfoService {

	void saveBoundInfo(Map<String, String> reqMap);

	void removeBoundInfo(Map<String, String> reqMap);

	BoundInfo findByFromUserAndStatus(String fromUser,String status);

	BoundInfo findByFromUserAndType(String fromUser, String wifi);

	List<BoundInfo> findByDeviceTypeAndFromUser(String string, String string2);

	List<BoundInfo> findByDeviceTypeAndFromUserNameOrderById(String appName,
			String openId);

	void removeBoundInfo(String fromUser, String deviceType);

	Map<String, String> getCmServerByDeviceId(Long deviceId);

 
}
