package iotwechat.xlink.cloud.repository;

import iotwechat.xlink.cloud.domain.Product;
import iotwechat.xlink.cloud.domain.ProductGateWayWx;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductGateWayWxRepository extends CrudRepository<ProductGateWayWx, Long>{
	ProductGateWayWx findByProductId(String id);
}
