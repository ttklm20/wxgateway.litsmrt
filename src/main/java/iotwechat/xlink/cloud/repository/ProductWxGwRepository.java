package iotwechat.xlink.cloud.repository;

import iotwechat.xlink.cloud.domain.ProductGateWayWx;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductWxGwRepository extends CrudRepository<ProductGateWayWx, Long>{
	ProductGateWayWx findByProductId(String productId);

	ProductGateWayWx findByProductIdAndAppName(String productId, String appName);

	ProductGateWayWx findByProductIdAndGateWayWxId(String pid, Long id);
}
