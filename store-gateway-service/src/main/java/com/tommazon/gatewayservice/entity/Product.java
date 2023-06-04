package com.tommazon.gatewayservice.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static javax.persistence.InheritanceType.SINGLE_TABLE;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Inheritance(strategy=SINGLE_TABLE)
@EntityListeners(AuditingEntityListener.class)
@DiscriminatorColumn(name = "class", discriminatorType = DiscriminatorType.STRING)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "version",  columnDefinition = "Long DEFAULT 0")
    @Version
    @JsonIgnore
    @Builder.Default
    Long version = 0L ;

    @Column(name = "uuid")
    @Builder.Default
    String uuid = String.valueOf(UUID.randomUUID());


    @Column(name = "sku")
    String sku;

//    @JsonIgnore
//    @Column(name = "class", updatable = false, insertable = false)
//    String clazz;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "price", nullable = false)
    BigDecimal price;

    @Column(name = "ccy", nullable = false)
    String ccy;

    @Column(name = "enabled", nullable = false)
    Boolean enabled;

    @CreatedDate
    @Column(name = "date_created", nullable = false, updatable = false)
    LocalDateTime dateCreated;

    @LastModifiedDate
    @Column(name="last_updated", nullable = false)
    LocalDateTime lastUpdated;

}