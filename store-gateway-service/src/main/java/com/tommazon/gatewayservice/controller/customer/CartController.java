package com.tommazon.gatewayservice.controller.customer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tommazon.gatewayservice.component.business.deal.DealCalculator;
import com.tommazon.gatewayservice.util.ReceiptJSONTableConverter;
import com.tommazon.gatewayservice.controller.AbstractRestController;
import com.tommazon.gatewayservice.controller.requestDto.CartRequest;
import com.tommazon.gatewayservice.controller.responseDto.CartCommitReceiptResponse;
import com.tommazon.gatewayservice.controller.responseDto.CartReceiptResponse;
import com.tommazon.gatewayservice.controller.responseDto.CartResponse;
import com.tommazon.gatewayservice.controller.responseDto.ProductResponse;
import com.tommazon.gatewayservice.entity.Cart;
import com.tommazon.gatewayservice.entity.Product;
import com.tommazon.gatewayservice.service.AdminService;
import com.tommazon.gatewayservice.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequestMapping("/api/customer")
@AllArgsConstructor
@RestController
public class CartController extends AbstractRestController {
    private final AdminService adminService;
    private final CustomerService customerService;
    private final DealCalculator dealCalculator;

    @PostMapping("/cart/add")
    public ResponseEntity<CartResponse> addToCart(@RequestBody CartRequest cartRequest, @RequestParam(required = false) String refId) {
        Long clientId = cartRequest.getClientId();
        Long productId = cartRequest.getProductId();
        if (productId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        var optionalProduct = adminService.getProductById(productId);
        if (optionalProduct.isPresent()) {
            var product = optionalProduct.get();
            var cb = Cart.builder();
            cb.product(product);
            cb.clientId(clientId);
            cb.ccy(product.getCcy());
            cb.price(product.getPrice());
            cb.quantity(1);

            var crb = CartResponse.builder();
            crb.items(List.of(customerService.addCart(cb.build(), clientId)));
            crb.refId(refId);
            crb.clientId(clientId);
            return ResponseEntity.status(HttpStatus.CREATED).body(crb.build());

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/cart/details/{clientId}/{itemId}")
    public ResponseEntity<CartResponse> getItemsDetail(@PathVariable Long clientId, @PathVariable Long itemId, @RequestParam(required = false) String refId) {
        var itemOpt = customerService.getCartByItemIdAndClientId(itemId, clientId);
        if (itemOpt.isPresent()) {
            var crb = CartResponse.builder();
            crb.items(List.of(itemOpt.get()));
            crb.refId(refId);
            crb.clientId(clientId);
            return ResponseEntity.status(HttpStatus.OK).body(crb.build());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    //get Receipt for open orders
    @GetMapping("/cart/{clientId}")
    public ResponseEntity<Object> getCartItems(@PathVariable Long clientId, @RequestParam(required = false) String refId, @RequestParam(required = false) boolean isNiceFormat) throws JsonProcessingException {
        var itemOpt = customerService.getCartItems(clientId);
        if (itemOpt.isPresent()) {
            BigDecimal totalPrice = dealCalculator.totalPrice(itemOpt.get());
            var ccy = "USD";
            var crb = CartReceiptResponse.builder();
            crb.items(itemOpt.get());
            crb.refId(refId);
            crb.clientId(clientId);
            crb.totalPrice(totalPrice);
            crb.ccy("USD");

            if (isNiceFormat) {
                ObjectMapper mapper = new ObjectMapper();
//                return ResponseEntity.status(HttpStatus.OK).body();
                List receiptItem =generateReceipt(itemOpt.get(), totalPrice, ccy);
                String jsonStr = mapper.writeValueAsString(receiptItem);
                Map result = Map.of("items", receiptItem, "print", ReceiptJSONTableConverter.convertJSONToTable(jsonStr));
                return ResponseEntity.status(HttpStatus.OK).body(result);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(crb.build());
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @DeleteMapping("/cart/{clientId}/{itemId}")
    public ResponseEntity<CartResponse> delete(@PathVariable Long clientId, @PathVariable Long itemId, @RequestParam(required = false) String refId) {
        var cart = customerService.deleteItemFromCart(itemId, clientId);
        if (cart.isPresent()) {
            var crb = CartResponse.builder();
            crb.items(List.of(cart.get()));
            crb.refId(refId);
            crb.clientId(clientId);
            return ResponseEntity.status(HttpStatus.OK).body(crb.build());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/cart/{clientId}/commit")
    public ResponseEntity<CartCommitReceiptResponse> commit(@PathVariable Long clientId, @RequestParam(required = false) String refId) {
        Optional<List<Cart>> opt = customerService.commitOpenOrders(clientId);
        if (opt.isPresent()) {
            var crb = CartCommitReceiptResponse.builder();
            crb.items(opt.get());
            crb.refId(refId);
            crb.clientId(clientId);
            crb.totalPrice(dealCalculator.totalPrice(opt.get()));
            crb.ccy("USD");
            crb.transactionUuid(opt.get().get(0).getTransactionUuid());
            return ResponseEntity.status(HttpStatus.OK).body(crb.build());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/product/all")
    public ResponseEntity<List<ProductResponse>> getAllActiveProducts(@RequestParam(required = false) String refId) {
        List<Product> products = adminService.getAllProductByEnabled(true);

        List<ProductResponse> prs = new ArrayList<>(products.size());
        var prb = ProductResponse.builder();
        products.forEach(it -> {
            prb.product(it);
            prb.refId(refId);
            prs.add(prb.build());
        });
        return ResponseEntity.ok(prs);
    }

    private List<Map<String, String>> generateReceipt(List<Cart> carts, BigDecimal totalPrice, String ccy) {
        List<Map<String, String>> l = new ArrayList<>();
        carts.forEach(cart -> {
            var pdOpt = customerService.getActiveDiscountByProductId(cart.getProduct().getId());
            Map item = Map.of("Name", cart.getProduct().getName(),
                    "Quantity", String.valueOf(cart.getQuantity()),
                    "Price", cart.getPrice().toPlainString(),
                    "Deal", cart.getDeal().toPlainString(),
                   "Remark", pdOpt.isPresent() &&  cart.getDeal().compareTo(new BigDecimal(0))>0 ?pdOpt.get().get(0).getConfigDiscount().getDescription():"");
            l.add(item);
        });

        l.add(Map.of("Name", "Total Price", "Price", totalPrice.toPlainString() +" "+ ccy));
        return l;
    }

}
