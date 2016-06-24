package iotwechat.xlink.cloud.repository;

import java.util.List;

import iotwechat.xlink.cloud.domain.Device;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends CrudRepository<Device, Long>{
	Device findById(Long id);

	List<Device> findByPid(String pid);

	// 查找数据库中没有TokenIds的条目
	List<Device> findByPidAndWxTokenIdsNotLike(String pid, String wxTokenIds);
	// 查找数据库中有TokenIds的条目
	List<Device> findByPidAndWxTokenIdsLike(String pid, String wxTokenIds);
 
	@Deprecated
	 @Modifying 
	 @Query("update Device d set d.wxTokenIds =d.wxTokenIds+',' +:tokenId where d.pid = :pid and d.wxTokenIds not like :tokenId") 
	 public int updateDeviceWxTokenIds(@Param("tokenId")String id,@Param("pid")String pid);

 
	
	
	
}
