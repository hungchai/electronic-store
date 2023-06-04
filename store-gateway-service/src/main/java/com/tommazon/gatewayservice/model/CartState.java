package com.tommazon.gatewayservice.model;

public enum CartState {
    OPEN,
    CANCELLED,
    COMMITTED, //after confirmed the receipt
    SETTLED  //after settled by creditcard
}
