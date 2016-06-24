package iotwechat.xlink.cloud.repository;

import iotwechat.xlink.cloud.domain.Product;
import iotwechat.xlink.cloud.domain.ProductGateWayWx;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, String>{
	Product findById(String id);
}
