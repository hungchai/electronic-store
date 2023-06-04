package com.tommazon.gatewayservice.component.business.deal;

import com.tommazon.gatewayservice.entity.Cart;
import com.tommazon.gatewayservice.entity.ProductDiscount;

import java.math.BigDecimal;
import java.util.List;

public interface DealCalculator {

    List<Cart> addItemDealCalculate(Cart addedItem, List<Cart> carts, List<ProductDiscount> productDiscountSettings);

    List<Cart> removeItemDealCalculate(Cart removedItem, List<Cart> carts, List<ProductDiscount> productDiscountSettings);

    BigDecimal totalPrice(List<Cart> carts);

//    List<Cart> reCalculateAll(List<Cart> carts, List<ProductDiscount> productDiscountSettings);
}
