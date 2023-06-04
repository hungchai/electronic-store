package com.tommazon.gatewayservice.controller.responseDto;

import com.tommazon.gatewayservice.entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class CartResponse extends BaseResponse{
    List<Cart> items;
    Long clientId;
}
