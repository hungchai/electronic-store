package com.tommazon.gatewayservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tommazon.gatewayservice.model.CartState;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static javax.persistence.InheritanceType.SINGLE_TABLE;

@Entity
@Table(name = "cart")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Inheritance(strategy = SINGLE_TABLE)
@EntityListeners(AuditingEntityListener.class)
@DiscriminatorColumn(name = "class", discriminatorType = DiscriminatorType.STRING)
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "version", columnDefinition = "Long DEFAULT 0")
    @Version
    @Builder.Default
    @JsonIgnore
    Long version = 0L;

    @Column(name = "client_id", nullable = false)
    Long clientId;

    //If settled, will add transaction uuid
    @Column(name = "transaction_uuid", nullable = true)
    String transactionUuid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    Product product;

    @Column(name = "quantity", nullable = false)
    int quantity;

//    @Column(name = "class", nullable = false, updatable = false, insertable = false)
//    @JsonIgnore
//    String clazz;

    @Column(name = "price", nullable = false)
    BigDecimal price;

    @Column(name = "deal")
    @Builder.Default
    BigDecimal deal = new BigDecimal(0);

    @Column(name = "ccy", nullable = false)
    String ccy;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    CartState state;

    @CreatedDate
    @Column(name = "date_created", nullable = false, updatable = false)
    LocalDateTime dateCreated;

    @LastModifiedDate
    @Column(name = "last_updated", nullable = false)
    LocalDateTime lastUpdated;
}
