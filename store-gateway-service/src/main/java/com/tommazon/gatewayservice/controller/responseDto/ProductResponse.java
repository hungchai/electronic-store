package com.tommazon.gatewayservice.controller.responseDto;

import com.tommazon.gatewayservice.entity.Product;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class ProductResponse extends BaseResponse {
    Product product;
}
