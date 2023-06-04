package com.tommazon.gatewayservice;

import com.tommazon.gatewayservice.controller.requestDto.CartRequest;
import com.tommazon.gatewayservice.controller.responseDto.CartCommitReceiptResponse;
import com.tommazon.gatewayservice.controller.responseDto.CartReceiptResponse;
import com.tommazon.gatewayservice.controller.responseDto.CartResponse;
import com.tommazon.gatewayservice.controller.responseDto.ProductResponse;
import com.tommazon.gatewayservice.entity.Cart;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@Order(2)
public class CartControllerTests extends AbstractTestBase {

    Map<String, String> urlMapping;

    @BeforeAll
    void setup() {
        urlMapping = Map.of(
                "addToCart", "http://localhost:" + serverPort + "/api/customer/cart/add?refId={refId}",
                "deleteItem", "http://localhost:" + serverPort + "/api/customer/cart/{clientId}/{itemId}?refId={refId}",
                "getAllProduct", "http://localhost:" + serverPort + "/api/customer/product/all?refId={refId}",
                "getCartItems", "http://localhost:" + serverPort + "/api/customer/cart/{clientId}?refId={refId}",
                "getCartItemsNiceFormat", "http://localhost:" + serverPort + "/api/customer/cart/{clientId}?refId={refId}&isNiceFormat=true",
                "getCartItemDetails", "http://localhost:" + serverPort + "/api/customer/cart/details/{clientId}/{itemId}?refId={refId}",
                "commitOpenCart", "http://localhost:" + serverPort + "/api/customer/cart/{clientId}/commit?refId={refId}"
        );
    }

    @Test
    @Order(1)
    void getAllActiveProductSuccess() {
        String url = urlMapping.get("getAllProduct");

        ResponseEntity<ProductResponse[]> response = restTemplate.getForEntity(url, ProductResponse[].class, Map.of("refId", refId));

        log.info(JSONArray.toJSONString(Arrays.asList(Objects.requireNonNull(response.getBody()))));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(10, response.getBody().length);
    }


    @Test
    @Order(2)
    void addToCart() {
        long clientId = 1L;

        //before add
        String url = urlMapping.get("getCartItems");
        ResponseEntity<CartResponse> response1 = restTemplate.getForEntity(url, CartResponse.class, Map.of("refId", refId, "clientId", clientId));

        List<Cart> currentCart = Objects.requireNonNull(response1.getBody()).getItems();
        assertEquals(3, currentCart.size());

        //add
        url = urlMapping.get("addToCart");
        long productId = 1L;

        var cartRequest = CartRequest.builder()
                .clientId(clientId)
                .productId(productId)
                .build();
        ResponseEntity<CartResponse> response2 = restTemplate.postForEntity(url, cartRequest, CartResponse.class, Map.of("refId", refId));
        assertEquals(HttpStatus.CREATED, response2.getStatusCode());

        assertEquals(refId, Objects.requireNonNull(response2.getBody()).getRefId());
        Cart cart = response2.getBody().getItems().get(0);
        assertEquals(Long.valueOf(clientId), cart.getClientId());
        assertEquals(Long.valueOf(productId), cart.getProduct().getId());

        productId = 5L;

        cartRequest = CartRequest.builder()
                .clientId(clientId)
                .productId(productId)
                .build();
        ResponseEntity<CartResponse> response3 = restTemplate.postForEntity(url, cartRequest, CartResponse.class, Map.of("refId", refId));
        assertEquals(HttpStatus.CREATED, response3.getStatusCode());

        assertEquals(refId, Objects.requireNonNull(response3.getBody()).getRefId());


        //after added
        url = urlMapping.get("getCartItems");
        ResponseEntity<CartResponse> response4 = restTemplate.getForEntity(url, CartResponse.class, Map.of("refId", refId, "clientId", clientId));

        List<Cart> finalCarts = Objects.requireNonNull(response4.getBody()).getItems();
        assertEquals(5, finalCarts.size());
    }


    @Test
    @Order(3)
    void deleteFromCart() {
        String url = urlMapping.get("getCartItems");
        long clientId = 2L;
        long itemId;

        ResponseEntity<CartResponse> response1 = restTemplate.getForEntity(url, CartResponse.class, Map.of("refId", refId, "clientId", clientId));
        List<Cart> currentCart = Objects.requireNonNull(response1.getBody()).getItems();
        assertEquals(5, currentCart.size());
        assertEquals(HttpStatus.OK, response1.getStatusCode());

        //random pick a item to remove from cart.
        itemId = Objects.requireNonNull(response1.getBody()).getItems().get(3).getId();

        url = urlMapping.get("deleteItem");
        restTemplate.delete(url, Map.of("refId", refId, "clientId", clientId, "itemId", itemId));

        url = urlMapping.get("getCartItems");
        ResponseEntity<CartResponse> response2 = restTemplate.getForEntity(url, CartResponse.class, Map.of("refId", refId, "clientId", clientId));
        assertEquals(HttpStatus.OK, response2.getStatusCode());

        List<Cart> finalCarts = Objects.requireNonNull(response2.getBody()).getItems();
        assertEquals(4, finalCarts.size());
        assertEquals(4, finalCarts.size());
    }

    @Test
    @Order(4)
    void getCartItems() {
        String url = urlMapping.get("getCartItems");
        long clientId = 1L;
        ResponseEntity<CartReceiptResponse> response1 = restTemplate.getForEntity(url, CartReceiptResponse.class, Map.of("refId", refId, "clientId", clientId));
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        CartReceiptResponse cr =  Objects.requireNonNull(response1.getBody());
        assertEquals(5, cr.getItems().size());
        assertEquals(0, new BigDecimal("10.65").compareTo(cr.getTotalPrice()));
        log.info("currency Cart of ClientId {}: {}",clientId, JSONArray.toJSONString(cr.getItems()));
        log.info("Total Price of ClientId {}: {}{}",clientId, cr.getTotalPrice(), cr.getCcy());
        url = urlMapping.get("getCartItemsNiceFormat");
        ResponseEntity<Map[]> response1NiceFormat = restTemplate.getForEntity(url, Map[].class, Map.of("refId", refId, "clientId", clientId, "isNiceFormat", true));
        assertEquals(HttpStatus.OK, response1NiceFormat.getStatusCode());
        log.info(JSONArray.toJSONString(Arrays.asList(Objects.requireNonNull(response1NiceFormat.getBody()))));

        clientId = 2L;
        url = urlMapping.get("getCartItems");
        ResponseEntity<CartReceiptResponse> response2 = restTemplate.getForEntity(url, CartReceiptResponse.class, Map.of("refId", refId, "clientId", clientId));
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        cr =  Objects.requireNonNull(response2.getBody());
        assertEquals(4, cr.getItems().size());
        assertEquals(0, new BigDecimal("3.66").compareTo(cr.getTotalPrice()));
        log.info("currency Cart of ClientId {}: {}",clientId, JSONArray.toJSONString(cr.getItems()));
        log.info("Total Price of ClientId {}: {}{}",clientId, cr.getTotalPrice(), cr.getCcy());
        url = urlMapping.get("getCartItemsNiceFormat");
        ResponseEntity<Map[]> response2NiceFormat = restTemplate.getForEntity(url, Map[].class, Map.of("refId", refId, "clientId", clientId, "isNiceFormat", true));
        assertEquals(HttpStatus.OK, response2NiceFormat.getStatusCode());
        log.info(JSONArray.toJSONString(Arrays.asList(Objects.requireNonNull(response2NiceFormat.getBody()))));


    }

    @Test
    @Order(5)
    void commit() {
        String url = urlMapping.get("commitOpenCart");
        long clientId = 1L;
        ResponseEntity<CartCommitReceiptResponse> response1 = restTemplate.postForEntity(url, null, CartCommitReceiptResponse.class, Map.of("refId", refId, "clientId", clientId));
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        CartCommitReceiptResponse cr =  Objects.requireNonNull(response1.getBody());
        assertEquals(5, cr.getItems().size());
        assertEquals(0, new BigDecimal("10.65").compareTo(cr.getTotalPrice()));
        assertNotNull(cr.getTransactionUuid());

        log.info("currency Cart of ClientId {}: {}",clientId, JSONArray.toJSONString(cr.getItems()));
        log.info("Total Price of ClientId {}: {}{}",clientId, cr.getTotalPrice(), cr.getCcy());
        log.info("Transaction id: {}{}",clientId, cr.getTransactionUuid());


        url = urlMapping.get("getCartItems");
        ResponseEntity<CartReceiptResponse> response2 = restTemplate.getForEntity(url, CartReceiptResponse.class, Map.of("refId", refId, "clientId", clientId));
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        CartReceiptResponse cr2 =  Objects.requireNonNull(response2.getBody());
        assertEquals(0, cr2.getItems().size());
        assertEquals(0, new BigDecimal("0").compareTo(cr2.getTotalPrice()));
        log.info("currency Cart of ClientId {}: {}",clientId, JSONArray.toJSONString(cr.getItems()));
        log.info("Total Price of ClientId {}: {}{}",clientId, cr.getTotalPrice(), cr.getCcy());
    }

}
