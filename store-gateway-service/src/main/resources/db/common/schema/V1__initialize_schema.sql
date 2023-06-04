DROP TABLE IF EXISTS `product`;

CREATE TABLE `product`
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    `version`    BIGINT(20) NOT NULL DEFAULT 1,
    uuid         varchar(255)            DEFAULT NULL,
    sku          varchar(20)    NOT NULL,
    `class`      varchar(255) NULL,
    name         VARCHAR(255)   NOT NULL,
    price        DECIMAL(19, 4) NOT NULL,
    ccy          varchar(5)     NOT NULL,
    enabled      bit(1)         NOT NULL DEFAULT 1,
    date_created datetime                default CURRENT_TIMESTAMP null,
    last_updated datetime                default CURRENT_TIMESTAMP null
);
CREATE UNIQUE INDEX idx_product_sku
    ON `product` (sku);
CREATE UNIQUE INDEX idx_product_uuid
    ON `product` (uuid);

DROP TABLE IF EXISTS config_discount;

CREATE TABLE config_discount
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    `version`           BIGINT(20) NOT NULL DEFAULT 1,
    uuid                VARCHAR(255) DEFAULT NULL,
    `class`             VARCHAR(255) NULL,
    description         VARCHAR(255) NOT NULL,
    discount_percentage TINYINT NOT NULL,
    minimum_order_qty TINYINT NOT NULL DEFAULT 1,
    enabled      BIT(1)       NOT NULL DEFAULT 1,
    date_created        DATETIME     DEFAULT CURRENT_TIMESTAMP NULL,
    last_updated        DATETIME     DEFAULT CURRENT_TIMESTAMP NULL
);
CREATE UNIQUE INDEX idx_config_discount_uuid
    ON `config_discount` (uuid);

CREATE UNIQUE INDEX idx_config_discount_discount_percentage
    ON `config_discount` (class, discount_percentage, minimum_order_qty);

DROP TABLE IF EXISTS product_discount;

CREATE TABLE product_discount
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    `version`    BIGINT(20) NOT NULL DEFAULT 1,
    uuid         VARCHAR(255)          DEFAULT NULL,
    `class`      VARCHAR(255) NULL,
    description  VARCHAR(255) NULL,
    product_id   BIGINT(20) NOT NULL,
    config_discount_id  BIGINT(20) NOT NULL,
    enabled      BIT(1)       NOT NULL DEFAULT 1,
    date_created DATETIME              DEFAULT CURRENT_TIMESTAMP NULL,
    last_updated DATETIME              DEFAULT CURRENT_TIMESTAMP NULL,
    FOREIGN KEY (product_id) REFERENCES product (id),
    FOREIGN KEY (config_discount_id) REFERENCES config_discount (id)
);

CREATE UNIQUE INDEX idx_product_discount_uuid
    ON `product_discount` (uuid);
