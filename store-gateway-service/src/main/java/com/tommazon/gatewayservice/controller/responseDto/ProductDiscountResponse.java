package com.tommazon.gatewayservice.controller.responseDto;

import com.tommazon.gatewayservice.entity.ProductDiscount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class ProductDiscountResponse extends BaseResponse {
    ProductDiscount productDiscount;
}
