package com.tommazon.gatewayservice;

import com.tommazon.gatewayservice.controller.responseDto.ConfigDiscountResponse;
import com.tommazon.gatewayservice.entity.ConfigDiscount;
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
public class ConfigDiscountControllerTests extends AbstractTestBase {
    Map<String, String> urlMapping;

    @BeforeAll
    void setup() {
        urlMapping = Map.of(
                "getAllConfigDiscount", "http://localhost:" + serverPort + "/api/admin/discount/config/all?refId={refId}",
                "createConfigDiscount", "http://localhost:" + serverPort + "/api/admin/discount/config?refId={refId}",
                "getConfigDiscountById", "http://localhost:" + serverPort + "/api/admin/discount/config/id/{configDiscountId}?refId={refId}",
                "updateConfigDiscount", "http://localhost:" + serverPort + "/api/admin/discount/config/id/{configDiscountId}?refId={refId}",
                "toggleConfigDiscount", "http://localhost:" + serverPort + "/api/admin/discount/config/id/{configDiscountId}/{enabled}?refId={refId}"
                );
    }

    @BeforeEach
    void reset() {

    }
    @Test
    @Order(1)
    void getAllConfigDiscountSuccess() {
        String url = urlMapping.get("getAllConfigDiscount");

        ResponseEntity<ConfigDiscountResponse[]> response = restTemplate.getForEntity(url, ConfigDiscountResponse[].class, Map.of("refId", refId));

        log.info("All ConfigDiscount {} ", JSONArray.toJSONString(Arrays.asList(Objects.requireNonNull(response.getBody()))));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5, response.getBody().length);
    }
    @Test
    @Order(2)
    void createConfigDiscountSuccess() {
        String url = urlMapping.get("createConfigDiscount");
        ConfigDiscount pdr = new ConfigDiscount();
        pdr.setDiscountPercentage(25);
        pdr.setEnabled(true);
        pdr.setMinimumOrderQty(2);
        pdr.setDescription("25% off from the second item");

        ResponseEntity<ConfigDiscountResponse> response = restTemplate.postForEntity(url, pdr, ConfigDiscountResponse.class, Map.of("refId", refId));

        log.info(JSONArray.toJSONString(List.of(Objects.requireNonNull(response.getBody()))));
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(6, response.getBody().getConfigDiscount().getId());
        assertEquals("25% off from the second item", response.getBody().getConfigDiscount().getDescription());


        url = urlMapping.get("getAllConfigDiscount");
        ResponseEntity<ConfigDiscountResponse[]> response1 = restTemplate.getForEntity(url, ConfigDiscountResponse[].class, Map.of("refId", refId));

        log.info("All ConfigDiscount {} ", JSONArray.toJSONString(List.of(Objects.requireNonNull(response.getBody()))));
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        assertEquals(6, Objects.requireNonNull(response1.getBody()).length);
        log.info(JSONArray.toJSONString(List.of(Objects.requireNonNull(response1.getBody()))));
    }

    @Test
    @Order(3)
    void getConfigDiscountById() {
        String url = urlMapping.get("getConfigDiscountById");
        ResponseEntity<ConfigDiscountResponse> response1 = restTemplate.getForEntity(url, ConfigDiscountResponse.class, Map.of("refId", refId, "configDiscountId", 6) );

        log.info("ConfigDiscount {} ", JSONArray.toJSONString(List.of(Objects.requireNonNull(response1.getBody()))));
        assertEquals(6, response1.getBody().getConfigDiscount().getId());
        assertEquals("25% off from the second item", response1.getBody().getConfigDiscount().getDescription());
        assertEquals(25, response1.getBody().getConfigDiscount().getDiscountPercentage());
        assertEquals(2, response1.getBody().getConfigDiscount().getMinimumOrderQty());

        assertEquals(HttpStatus.OK, response1.getStatusCode());
        log.info(JSONArray.toJSONString(List.of(Objects.requireNonNull(response1.getBody()))));
    }

    @Test
    @Order(4)
    void toggleOffConfigDiscountById() {
        String url = urlMapping.get("toggleConfigDiscount");

        ResponseEntity<ConfigDiscountResponse> response1 =restTemplate.exchange(url, HttpMethod.PUT, null, ConfigDiscountResponse.class, Map.of("refId", refId, "enabled", "false","configDiscountId", 6));

        log.info("toggle off {} ", JSONArray.toJSONString(List.of(Objects.requireNonNull(response1.getBody()))));
        assertEquals(6, response1.getBody().getConfigDiscount().getId());
        assertEquals(false, response1.getBody().getConfigDiscount().getEnabled());
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        log.info(JSONArray.toJSONString(List.of(Objects.requireNonNull(response1.getBody()))));
    }
    @Test
    @Order(4)
    void updateConfigDiscountById() {
        String url = urlMapping.get("updateConfigDiscount");
        ConfigDiscount pdr = new ConfigDiscount();
        pdr.setDiscountPercentage(35);
        pdr.setMinimumOrderQty(3);
        pdr.setDescription("35% off from the second item");

        Long pdId = 6L;
        HttpEntity<ConfigDiscount> httpEntity = getEntity(pdr);

        ResponseEntity<ConfigDiscountResponse> response1 =restTemplate.exchange(url, HttpMethod.PUT, httpEntity, ConfigDiscountResponse.class, Map.of("refId", refId,"configDiscountId", pdId));

        log.info("update {} ", JSONArray.toJSONString(List.of(Objects.requireNonNull(response1.getBody()))));
        assertEquals(6, response1.getBody().getConfigDiscount().getId());
        assertEquals(false, response1.getBody().getConfigDiscount().getEnabled());
        assertEquals(3, response1.getBody().getConfigDiscount().getMinimumOrderQty());
        assertEquals(35, response1.getBody().getConfigDiscount().getDiscountPercentage());
        assertEquals("35% off from the second item", response1.getBody().getConfigDiscount().getDescription());
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        log.info(JSONArray.toJSONString(List.of(Objects.requireNonNull(response1.getBody()))));


        url = urlMapping.get("getConfigDiscountById");
        ResponseEntity<ConfigDiscountResponse> response2 = restTemplate.getForEntity(url, ConfigDiscountResponse.class, Map.of("refId", refId, "configDiscountId", pdId) );

        log.info("ConfigDiscount {} ", JSONArray.toJSONString(List.of(Objects.requireNonNull(response1.getBody()))));
        assertEquals(6, Objects.requireNonNull(response2.getBody()).getConfigDiscount().getId());
        assertEquals(false, response2.getBody().getConfigDiscount().getEnabled());
        assertEquals(3, response2.getBody().getConfigDiscount().getMinimumOrderQty());
        assertEquals(35, response2.getBody().getConfigDiscount().getDiscountPercentage());
        assertEquals("35% off from the second item", response2.getBody().getConfigDiscount().getDescription());
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        log.info(JSONArray.toJSONString(List.of(Objects.requireNonNull(response2.getBody()))));
    }
}
