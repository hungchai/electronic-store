package com.tommazon.gatewayservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import static javax.persistence.InheritanceType.SINGLE_TABLE;

@Entity
@Table(name = "config_discount")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = SINGLE_TABLE)
@EntityListeners(AuditingEntityListener.class)
@DiscriminatorColumn(name = "class", discriminatorType = DiscriminatorType.STRING)
public abstract class AbstractConfigDiscount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "version", columnDefinition = "Long DEFAULT 0")
    @Version
    @Builder.Default
    @JsonIgnore
    Long version = 0L;

    @Builder.Default
    @Column(name = "uuid")
    String uuid = String.valueOf(UUID.randomUUID());

//    @JsonIgnore
//    @Column(name = "class", updatable = false, insertable = false)
//    String clazz;

    @Column(name = "description", nullable = false)
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
