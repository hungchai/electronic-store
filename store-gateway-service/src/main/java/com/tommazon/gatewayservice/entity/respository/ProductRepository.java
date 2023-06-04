package com.tommazon.gatewayservice.entity.respository;

import com.tommazon.gatewayservice.entity.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findAllByEnabled(Boolean enabled);
    Product findBySku(String sku);
}
