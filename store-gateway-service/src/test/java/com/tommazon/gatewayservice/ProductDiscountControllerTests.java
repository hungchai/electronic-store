package com.tommazon.gatewayservice;

import com.tommazon.gatewayservice.controller.requestDto.ProductDiscountRequest;
import com.tommazon.gatewayservice.controller.responseDto.ProductDiscountResponse;
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@Order(1)
public class ProductDiscountControllerTests extends AbstractTestBase {
    Map<String, String> urlMapping;

    @BeforeAll
    void setup() {
        urlMapping = Map.of(
                "getAllProductDiscount", "http://localhost:" + serverPort + "/api/admin/productdiscount/all?refId={refId}",
                "createProductDiscount", "http://localhost:" + serverPort + "/api/admin/productdiscount?refId={refId}",
                "getProductDiscountById", "http://localhost:" + serverPort + "/api/admin/productdiscount/id/{productDiscountId}?refId={refId}",
                "updateProductDiscount", "http://localhost:" + serverPort + "/api/admin/productdiscount/id/{productDiscountId}?refId={refId}",
                "toggleProductDiscount", "http://localhost:" + serverPort + "/api/admin/productdiscount/id/{productDiscountId}/{enabled}?refId={refId}"
                );


    }

    @BeforeEach
    void reset()
    {

    }
    @Test
    @Order(1)
    void getAllProductDiscountSuccess() {
        String url = urlMapping.get("getAllProductDiscount");

        ResponseEntity<ProductDiscountResponse[]> response = restTemplate.getForEntity(url, ProductDiscountResponse[].class, Map.of("refId", refId));

        log.info("All ProductDiscount {} ", JSONArray.toJSONString(Arrays.asList(Objects.requireNonNull(response.getBody()))));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(4, response.getBody().length);
    }
    @Test
    @Order(2)
    void createProductDiscountSuccess() {
        String url = urlMapping.get("createProductDiscount");
        ProductDiscountRequest pdr = new ProductDiscountRequest();
        pdr.setProductId(4L);
        pdr.setConfigDiscountId(2L);
        pdr.setEnabled(true);

        ResponseEntity<ProductDiscountResponse> response = restTemplate.postForEntity(url, pdr, ProductDiscountResponse.class, Map.of("refId", refId));

        log.info(JSONArray.toJSONString(List.of(Objects.requireNonNull(response.getBody()))));
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(5, response.getBody().getProductDiscount().getId());
        assertEquals(4, response.getBody().getProductDiscount().getProduct().getId());
        assertEquals(2, response.getBody().getProductDiscount().getConfigDiscount().getId());


        url = urlMapping.get("getAllProductDiscount");
        ResponseEntity<ProductDiscountResponse[]> response1 = restTemplate.getForEntity(url, ProductDiscountResponse[].class, Map.of("refId", refId));

        log.info("All ProductDiscount {} ", JSONArray.toJSONString(List.of(Objects.requireNonNull(response.getBody()))));
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        assertEquals(5, Objects.requireNonNull(response1.getBody()).length);
        log.info(JSONArray.toJSONString(List.of(Objects.requireNonNull(response1.getBody()))));
    }

    @Test
    @Order(3)
    void getProductDiscountById() {
        String url = urlMapping.get("getProductDiscountById");
        ResponseEntity<ProductDiscountResponse> response1 = restTemplate.getForEntity(url, ProductDiscountResponse.class, Map.of("refId", refId, "productDiscountId", 5) );

        log.info("ProductDiscount {} ", JSONArray.toJSONString(List.of(Objects.requireNonNull(response1.getBody()))));
        assertEquals(5, response1.getBody().getProductDiscount().getId());
        assertEquals(4, response1.getBody().getProductDiscount().getProduct().getId());
        assertEquals(2, response1.getBody().getProductDiscount().getConfigDiscount().getId());

        assertEquals(HttpStatus.OK, response1.getStatusCode());
        log.info(JSONArray.toJSONString(List.of(Objects.requireNonNull(response1.getBody()))));
    }

    @Test
    @Order(4)
    void toggleOffProductDiscountById() {
        String url = urlMapping.get("toggleProductDiscount");

        ResponseEntity<ProductDiscountResponse> response1 =restTemplate.exchange(url, HttpMethod.PUT, null, ProductDiscountResponse.class, Map.of("refId", refId, "enabled", "false","productDiscountId", 5));

        log.info("toggle off {} ", JSONArray.toJSONString(List.of(Objects.requireNonNull(response1.getBody()))));
        assertEquals(5, response1.getBody().getProductDiscount().getId());
        assertEquals(false, response1.getBody().getProductDiscount().getEnabled());
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        log.info(JSONArray.toJSONString(List.of(Objects.requireNonNull(response1.getBody()))));
    }
    @Test
    @Order(4)
    void updateProductDiscountById() {
        String url = urlMapping.get("updateProductDiscount");
        ProductDiscountRequest pdr = new ProductDiscountRequest();
        pdr.setConfigDiscountId(1L);
        Long pdId = 5L;
        HttpEntity<ProductDiscountRequest> httpEntity = getEntity(pdr);

        ResponseEntity<ProductDiscountResponse> response1 =restTemplate.exchange(url, HttpMethod.PUT, httpEntity, ProductDiscountResponse.class, Map.of("refId", refId, "productDiscountId", pdId));

        log.info("update {} ", JSONArray.toJSONString(List.of(Objects.requireNonNull(response1.getBody()))));
        assertEquals(5, response1.getBody().getProductDiscount().getId());
        assertEquals(false, response1.getBody().getProductDiscount().getEnabled());
        assertEquals(1, response1.getBody().getProductDiscount().getConfigDiscount().getId());
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        log.info(JSONArray.toJSONString(List.of(Objects.requireNonNull(response1.getBody()))));


        url = urlMapping.get("getProductDiscountById");
        ResponseEntity<ProductDiscountResponse> response2 = restTemplate.getForEntity(url, ProductDiscountResponse.class, Map.of("refId", refId, "productDiscountId", pdId) );

        log.info("ProductDiscount {} ", JSONArray.toJSONString(List.of(Objects.requireNonNull(response1.getBody()))));
        assertEquals(5, Objects.requireNonNull(response2.getBody()).getProductDiscount().getId());
        assertEquals(false, response2.getBody().getProductDiscount().getEnabled());
        assertEquals(1, response2.getBody().getProductDiscount().getConfigDiscount().getId());

        assertEquals(HttpStatus.OK, response2.getStatusCode());
        log.info(JSONArray.toJSONString(List.of(Objects.requireNonNull(response2.getBody()))));

    }
}
