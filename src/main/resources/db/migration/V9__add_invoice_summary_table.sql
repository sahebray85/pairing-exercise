DROP TABLE IF EXISTS organisations_schema.invoice_summary
CREATE TABLE IF NOT EXISTS organisations_schema.invoice_summary
(
    id                 UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    buyer_id           VARCHAR(50) NOT NULL,  // We assume only email for consumer identification
    merchant_id        UUID   NOT NULL,
    order_id           VARCHAR(50),
    total_amount       FLOAT(10, 2) NOT NULL,
    currency_code      VARCHAR(3) NOT NULL,
    order_created      TIMESTAMP NOT NULL,
    order_updated      TIMESTAMP NOT NULL
 );
