package com.tommazon.gatewayservice.controller.requestDto;

import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;

@Data
@Builder
public class CartRequest {
    Long productId;

    @NonNull
    Long clientId;
}
