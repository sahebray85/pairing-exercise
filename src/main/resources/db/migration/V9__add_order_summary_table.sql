CREATE TABLE IF NOT EXISTS organisations_schema.order_summary
(
    id                 UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    buyer_id           VARCHAR(50) NOT NULL,
    merchant_id        UUID        NOT NULL,
    total_amount       DECIMAL(10, 2) NOT NULL,
    currency_code      VARCHAR(3) NOT NULL,
    order_created      TIMESTAMP NOT NULL
 );
