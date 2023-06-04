package com.tommazon.gatewayservice;

import com.tommazon.gatewayservice.controller.responseDto.ProductResponse;
import com.tommazon.gatewayservice.entity.Product;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@Order(1)
public class ProductControllerTests extends AbstractTestBase {
    Map<String, String> urlMapping;
    Product.ProductBuilder pb = Product.builder();

    @BeforeAll
    void setup() {
        urlMapping = Map.of(
                "getAllProduct", "http://localhost:" + serverPort + "/api/admin/product/all?enabled={enabled}&refId={refId}",
                "createProduct", "http://localhost:" + serverPort + "/api/admin/product?refId={refId}",
                "updateProduct", "http://localhost:" + serverPort + "/api/admin/product/id/{productId}?refId={refId}",
                "deleteProduct", "http://localhost:" + serverPort + "/api/admin/product/id/{productId}?refId={refId}",
                "getProductById", "http://localhost:" + serverPort + "/api/admin/product/id/{productId}?refId={refId}",
                "getProductBySku", "http://localhost:" + serverPort + "/api/admin/product/sku/{sku}?refId={refId}"
        );


    }

    @BeforeEach
    void reset()
    {
        pb.enabled(true);
        pb.name("D101 Durian");
        pb.price(new BigDecimal("11.09"));
        pb.ccy("USD");
    }
    @Test
    @Order(1)
    void getAllProductSuccess() {
        String url = urlMapping.get("getAllProduct");

        ResponseEntity<ProductResponse[]> response = restTemplate.getForEntity(url, ProductResponse[].class, Map.of("refId", refId, "enabled", "true"));

        log.info("All products {} ", JSONArray.toJSONString(Arrays.asList(Objects.requireNonNull(response.getBody()))));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(10, response.getBody().length);
    }

    @Test
    @Order(2)
    void createProductSuccess() {
        String url = urlMapping.get("createProduct");
        String sku = "SKU-12";
        pb.sku(sku);
        var p = pb.build();
        ResponseEntity<ProductResponse> response = restTemplate.postForEntity(url, p, ProductResponse.class, Map.of("refId", refId));

        log.info(JSONArray.toJSONString(List.of(Objects.requireNonNull(response.getBody()))));
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        assertEquals(refId, response.getBody().getRefId());
        Long id = response.getBody().getProduct().getId();

        url = urlMapping.get("getProductBySku");
        ResponseEntity<ProductResponse> response2 = restTemplate.getForEntity(url, ProductResponse.class, Map.of("refId", refId, "sku", sku));
        ProductResponse productResp = response2.getBody();

        assertEquals(p.getName(), productResp.getProduct().getName());
        assertEquals(p.getSku(), productResp.getProduct().getSku());
        assertEquals(0, p.getPrice().compareTo(productResp.getProduct().getPrice()));
        assertEquals(p.getCcy(), productResp.getProduct().getCcy());
        assertEquals(p.getEnabled(), productResp.getProduct().getEnabled());

        url = urlMapping.get("getProductById");
        ResponseEntity<ProductResponse> response3 = restTemplate.getForEntity(url, ProductResponse.class, Map.of("refId", refId, "productId", id));
        productResp = response3.getBody();

        assertEquals(p.getName(), productResp.getProduct().getName());
        assertEquals(p.getSku(), productResp.getProduct().getSku());
        assertEquals(0, p.getPrice().compareTo(productResp.getProduct().getPrice()));
        assertEquals(p.getCcy(), productResp.getProduct().getCcy());
        assertEquals(p.getEnabled(), productResp.getProduct().getEnabled());
    }

    @Test
    @Order(3)
    void createProductFail() {
        String url = urlMapping.get("createProduct");
        var pb = Product.builder();
        var p = pb.build();
        ResponseEntity<ProductResponse> response = restTemplate.postForEntity(url, p, ProductResponse.class, Map.of("refId", refId));

        log.info(JSONArray.toJSONString(List.of(Objects.requireNonNull(response.getBody()))));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    @Order(4)
    void updateProductSuccess() {
        String url = urlMapping.get("createProduct");
        String sku = "SKU-13";
        pb.sku(sku);
        pb.name("D102");

        var p = pb.build();
        ResponseEntity<ProductResponse> response = restTemplate.postForEntity(url, p, ProductResponse.class, Map.of("refId", refId));

        log.info(JSONArray.toJSONString(List.of(Objects.requireNonNull(response.getBody()))));
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Long id = response.getBody().getProduct().getId();
        var updateProd = response.getBody().getProduct();
        updateProd.setPrice(new BigDecimal("12.09"));

        url = urlMapping.get("updateProduct");
        HttpEntity<Product> httpEntity = getEntity(updateProd);
        ResponseEntity<ProductResponse> productResp = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, ProductResponse.class, Map.of("refId", refId, "productId", id));
        assertEquals(HttpStatus.OK, productResp.getStatusCode());

        assertEquals(0, updateProd.getPrice().compareTo(Objects.requireNonNull(productResp.getBody()).getProduct().getPrice()));
    }

    @Test
    @Order(5)
    void disableProductSuccess() {
        String url = urlMapping.get("deleteProduct");
        Long productId = 10L;
        String refId = "deleteitem 10";

        restTemplate.delete(url, Map.of("refId", refId, "productId", productId));

        url = urlMapping.get("getProductById");
        ResponseEntity<ProductResponse> response = restTemplate.getForEntity(url, ProductResponse.class, Map.of("refId", refId, "productId", productId));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(false, Objects.requireNonNull(response.getBody()).getProduct().getEnabled());
    }
}
