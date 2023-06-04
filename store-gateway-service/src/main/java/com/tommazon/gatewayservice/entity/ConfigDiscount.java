package com.tommazon.gatewayservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@DiscriminatorValue("ConfigDiscount")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ConfigDiscount extends AbstractConfigDiscount{

    @Column(name = "discount_percentage", nullable = false)
    Integer discountPercentage;

    @Column(name = "minimum_order_qty", nullable = false)
    Integer minimumOrderQty;

}
