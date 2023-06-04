package com.tommazon.gatewayservice.service;

import com.tommazon.gatewayservice.component.business.deal.DealCalculator;
import com.tommazon.gatewayservice.entity.Cart;
import com.tommazon.gatewayservice.entity.ProductDiscount;
import com.tommazon.gatewayservice.entity.respository.CartRepository;
import com.tommazon.gatewayservice.entity.respository.ProductDiscountRepository;
import com.tommazon.gatewayservice.entity.respository.ProductRepository;
import com.tommazon.gatewayservice.model.CartState;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService extends BaseService {
    private final CartRepository cartRepository;
    private final ProductDiscountRepository productDiscountRepository;
    private final DealCalculator discountDealCalculator;
    private final List<CartState> allowDeleteStates = List.of(CartState.OPEN);

    public CustomerService(ProductRepository productRepository, CartRepository cartRepository, ProductDiscountRepository productDiscountRepository, DealCalculator discountDealCalculator) {
        super(productRepository);
        this.cartRepository = cartRepository;
        this.productDiscountRepository = productDiscountRepository;
        this.discountDealCalculator = discountDealCalculator;
    }

    @Transactional
    public Cart addCart(Cart newItem, Long clientId) {
        newItem.setState(CartState.OPEN);
        List<ProductDiscount> productDiscounts = productDiscountRepository.findAllByProductId(newItem.getProduct().getId());
        List<Cart> carts = getCartItems(clientId).orElse(Collections.emptyList());
        List<Cart> reCalcCarts = discountDealCalculator.addItemDealCalculate(newItem, carts, productDiscounts);
        cartRepository.saveAll(reCalcCarts);
        return newItem;
    }

    @Transactional
    public Optional<Cart> deleteItemFromCart(Long itemId, Long clientId) {
        var removedItem = cartRepository.findByIdAndClientIdAndStateIn(itemId, clientId, allowDeleteStates);
        if (removedItem != null) {
            List<Cart> carts = getCartItems(clientId).orElse(Collections.emptyList());
            List<ProductDiscount> productDiscounts = productDiscountRepository.findAllByProductId(removedItem.getProduct().getId());
            List<Cart> reCalcCarts = discountDealCalculator.removeItemDealCalculate(removedItem, carts, productDiscounts);
            cartRepository.saveAll(reCalcCarts);
            cartRepository.delete(removedItem);
            return Optional.of(removedItem);
        } else
            return Optional.empty();
    }

    @Transactional
    public Optional<Cart> getCartByItemIdAndClientId(Long itemId, Long clientId) {
        return Optional.ofNullable(cartRepository.findByIdAndClientId(itemId, clientId));
    }

    @Transactional
    public Optional<List<Cart>> getCartItems(Long clientId) {
        return Optional.ofNullable(cartRepository.findAllByClientIdAndState(clientId, CartState.OPEN));
    }

    @Transactional
    public Optional<List<ProductDiscount>> getActiveDiscountByProductId(Long productId) {
        return Optional.ofNullable(productDiscountRepository.findAllByProductId(productId));
    }
    @Transactional
    public Optional<List<Cart>> commitOpenOrders(Long clientId) {
        String transactionUUid = UUID.randomUUID().toString();
        Optional<List<Cart>> openCartOpt = getCartItems(clientId);
        if (openCartOpt.isPresent()) {
            List<Cart> carts = openCartOpt.get();
            CartState committed = CartState.COMMITTED;
            carts.forEach(cart -> {
                        cart.setTransactionUuid(transactionUUid);
                        cart.setState(committed);
                    }
            );
            cartRepository.saveAll(carts);
            return Optional.of(carts);
        } else
            return Optional.empty();
    }
}
