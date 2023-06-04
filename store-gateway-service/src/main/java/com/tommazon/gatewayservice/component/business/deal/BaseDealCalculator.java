package com.tommazon.gatewayservice.component.business.deal;

import com.tommazon.gatewayservice.entity.Cart;

import java.math.BigDecimal;
import java.util.List;

public class BaseDealCalculator {
    public BigDecimal totalPrice(List<Cart> carts) {
        return carts.stream()
                .map(cart -> cart.getPrice().subtract(cart.getDeal()))
                .reduce(new BigDecimal(0), BigDecimal::add);
    }
}
