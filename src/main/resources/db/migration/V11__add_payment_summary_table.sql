CREATE TABLE IF NOT EXISTS organisations_schema.payment_summary
(
    id                 UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    invoice_id         UUID        NOT NULL,
    payment_amount     DECIMAL(10, 2) NOT NULL,
    currency_code      VARCHAR(3) NOT NULL,
    payment_date       TIMESTAMP NOT NULL
 );
