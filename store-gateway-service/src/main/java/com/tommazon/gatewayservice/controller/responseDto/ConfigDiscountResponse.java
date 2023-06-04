package com.tommazon.gatewayservice.controller.responseDto;

import com.tommazon.gatewayservice.entity.ConfigDiscount;
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
public class ConfigDiscountResponse extends BaseResponse {
    ConfigDiscount configDiscount;
}
