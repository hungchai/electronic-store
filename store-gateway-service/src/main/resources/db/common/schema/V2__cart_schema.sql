DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    `version`        BIGINT(20) NOT NULL DEFAULT 1,
    client_id        BIGINT(20) NOT NULL,
    transaction_uuid varchar(255)   NULL,
    product_id       BIGINT(20) NOT NULL,
    quantity         BIGINT(20) NOT NULL,
    `class`          VARCHAR(255)   NULL,
    price            DECIMAL(19, 4) NOT NULL,
    deal             DECIMAL(19, 4) NULL,
    ccy              varchar(5)     NOT NULL,
    state            VARCHAR(50)    NOT NULL,
    date_created     datetime default CURRENT_TIMESTAMP null,
    last_updated     datetime default CURRENT_TIMESTAMP null
);
CREATE INDEX idx_cart_client_id
    ON `cart` (client_id);