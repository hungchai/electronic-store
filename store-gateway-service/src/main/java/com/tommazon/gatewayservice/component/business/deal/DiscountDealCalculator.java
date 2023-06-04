package com.tommazon.gatewayservice.component.business.deal;


import com.tommazon.gatewayservice.entity.Cart;
import com.tommazon.gatewayservice.entity.ConfigDiscount;
import com.tommazon.gatewayservice.entity.ProductDiscount;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class DiscountDealCalculator extends BaseDealCalculator implements DealCalculator{

    public List<Cart> addItemDealCalculate(Cart addedItem, List<Cart> carts, List<ProductDiscount> productDiscountSettings){
        Long productId = addedItem.getProduct().getId();
        ConfigDiscount pd = productDiscountSettings.get(0).getConfigDiscount();
        carts.add(addedItem);
        return doDealReset(productId, carts, pd);
    }

    private List<Cart> doDealReset(Long productId, List<Cart> carts, ConfigDiscount pd ) {
        AtomicInteger qty = new AtomicInteger();
        carts.forEach(cart -> {
            if (cart.getProduct().getId().compareTo(productId) == 0) {
                qty.getAndIncrement();
                if (qty.get() >= pd.getMinimumOrderQty()) {
                    cart.setDeal(new BigDecimal(pd.getDiscountPercentage())
                            .divide(new BigDecimal(100), 2,  RoundingMode.HALF_UP)
                            .multiply(cart.getPrice())
                            .setScale(2, RoundingMode.HALF_UP));
                } else {
                    cart.setDeal(new BigDecimal(0));
                }
            }
        });
        return carts;
    }

    public List<Cart> removeItemDealCalculate(Cart removedItem, List<Cart> carts, List<ProductDiscount> productDiscountSettings){
        Long productId = removedItem.getProduct().getId();
        ConfigDiscount pd = productDiscountSettings.get(0).getConfigDiscount();
        carts.remove(removedItem);
        return doDealReset(productId, carts, pd);
    }

}
