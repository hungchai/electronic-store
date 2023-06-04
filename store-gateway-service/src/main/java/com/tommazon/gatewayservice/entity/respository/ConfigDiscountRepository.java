package com.tommazon.gatewayservice.entity.respository;

import com.tommazon.gatewayservice.entity.ConfigDiscount;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ConfigDiscountRepository extends CrudRepository<ConfigDiscount, Long> {

    List<ConfigDiscount> findAllByEnabled(Boolean enabled);

}
