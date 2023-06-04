package com.tommazon.gatewayservice.controller.responseDto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Data
public class BaseResponse {
    String refId;
    String remark;
}
