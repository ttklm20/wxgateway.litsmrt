package iotwechat.xlink.cloud.repository;

import iotwechat.xlink.cloud.domain.GateWayWx;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WxGateWayRepository extends CrudRepository<GateWayWx, Long> {

	GateWayWx findByTokenId(String tokenId);
	
}
