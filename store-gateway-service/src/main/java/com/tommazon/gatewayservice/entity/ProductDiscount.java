package com.tommazon.gatewayservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import static javax.persistence.InheritanceType.SINGLE_TABLE;

@Entity
@Table(name = "product_discount")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Inheritance(strategy=SINGLE_TABLE)
@EntityListeners(AuditingEntityListener.class)
@DiscriminatorColumn(name = "class", discriminatorType = DiscriminatorType.STRING)
public class ProductDiscount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "version",  columnDefinition = "Long DEFAULT 0")
    @Version
    @JsonIgnore
    @Builder.Default
    Long version  = 0L;

    @Builder.Default
    @Column(name = "uuid")
    String uuid = String.valueOf(UUID.randomUUID());

//    @JsonIgnore
//    @Column(name = "class", updatable = false, insertable = false)
//    String clazz;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "config_discount_id")
    ConfigDiscount configDiscount;

    @Column(name = "description")
    String description;

    @Column(name = "enabled", nullable = false)
    Boolean enabled;

    @CreatedDate
    @Column(name = "date_created", nullable = false, updatable = false)
    LocalDateTime dateCreated;

    @LastModifiedDate
    @Column(name = "last_updated", nullable = false)
    LocalDateTime lastUpdated;
}
