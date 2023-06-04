package com.tommazon.gatewayservice.entity.respository;

import com.tommazon.gatewayservice.entity.ConfigDiscount;
import com.tommazon.gatewayservice.entity.ProductDiscount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductDiscountRepository extends CrudRepository<ProductDiscount, Long> {
    List<ProductDiscount> findAllByEnabled(Boolean enabled);
    List<ProductDiscount> findAllByConfigDiscount(ConfigDiscount cd);

    @Query(value = "select pd from ProductDiscount pd where pd.product.id = :productId AND pd.enabled = true")
    List<ProductDiscount> findAllByProductId(Long productId);

}
