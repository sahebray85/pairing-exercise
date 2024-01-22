CREATE TABLE IF NOT EXISTS organisations_schema.invoice_summary
(
    id                 UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    order_id           UUID        NOT NULL,
    invoice_amount     DECIMAL(10, 2) NOT NULL,
    currency_code      VARCHAR(3) NOT NULL,
    invoice_status     VARCHAR(10) NOT NULL,
    invoice_created      TIMESTAMP NOT NULL,
    invoice_updated      TIMESTAMP NOT NULL
 );

ALTER TABLE organisations_schema.invoice_summary
ADD CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES organisations_schema.order_summary(id);
