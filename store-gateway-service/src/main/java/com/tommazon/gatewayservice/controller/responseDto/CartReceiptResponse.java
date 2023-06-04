package com.tommazon.gatewayservice.controller.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class CartReceiptResponse extends CartResponse{
    BigDecimal totalPrice;
    String ccy;
    String receipt;
}
