package com.tommazon.gatewayservice.controller.requestDto;

import lombok.Data;

@Data
public class ProductDiscountRequest {
    Long productId;
    Long configDiscountId;
    String description;
    Boolean enabled;
}
