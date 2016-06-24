package iotwechat.xlink.cloud.repository;

import java.util.List;

import iotwechat.xlink.cloud.domain.BoundInfo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface BoundInfoRepository  extends CrudRepository<BoundInfo, Long> {
	
	public BoundInfo findByFromUserName(String fromUserName);

	public BoundInfo findByFromUserNameAndStatus(String fromUser, String status);
	
	public BoundInfo findByFromUserNameAndDeviceID(String fromUser, String deviceID);

	public BoundInfo findByFromUserNameAndType(String fromUser, String type);

	public BoundInfo findByDeviceTypeAndFromUserNameAndDeviceID(String deviceType,
			String fromUser, String deviceID);

	public List<BoundInfo> findByDeviceTypeAndFromUserName(String deviceType,
			String fromUser);

	public List<BoundInfo> findByDeviceTypeAndFromUserNameOrderByIdAsc(String deviceType,
			String fromUser);

	public List<BoundInfo> findByDeviceTypeAndFromUserNameOrderByIdDesc(
			String appName, String openId);

	public BoundInfo findByDeviceTypeAndDeviceID(String string, long parseLong);
}
