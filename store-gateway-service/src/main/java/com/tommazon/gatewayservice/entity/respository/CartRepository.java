package com.tommazon.gatewayservice.entity.respository;

import com.tommazon.gatewayservice.entity.Cart;
import com.tommazon.gatewayservice.model.CartState;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CartRepository extends CrudRepository<Cart, Long> {
    List<Cart> findAllByClientIdAndState(Long clientId, CartState cartState);
    @Query(value = "select c from Cart c where c.product.id = :productId AND c.clientId = :clientId And c.state in :cartState ")
    List<Cart> findAllByProductIdAndClientIdAndStateIn(Long productId, Long clientId, List<CartState> cartState);
    @Query(value = "select c from Cart c where c.product.id = :productId AND c.clientId = :clientId")
    List<Cart> findAllByProductIdAndClientId(Long productId, Long clientId);

    Cart findByIdAndClientId(Long cartId, Long clientId);
    Cart findByIdAndClientIdAndStateIn(Long cartId, Long clientId, List<CartState> cartState);

}
