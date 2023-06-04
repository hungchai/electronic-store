DROP TABLE IF EXISTS `journal_entry`;
CREATE TABLE `journal_entry`
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    `version`    BIGINT(20) NOT NULL DEFAULT 1,
    transaction_uuid varchar(255) NOT NULL,
    product_id   BIGINT(20) NOT NULL,
    quantity     BIGINT(20) NOT NULL,
    `class`      VARCHAR(255) NOT NULL,
    total       DECIMAL(19, 4),
    ccy          varchar(5)  NOT NULL,
    date_created datetime default CURRENT_TIMESTAMP null,
    last_updated datetime default CURRENT_TIMESTAMP null
);

CREATE INDEX idx_journal_entry_transaction_uuid
    ON `journal_entry` (transaction_uuid);