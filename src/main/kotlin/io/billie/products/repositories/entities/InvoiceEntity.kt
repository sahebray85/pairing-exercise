package io.billie.products.repositories.entities

import com.fasterxml.jackson.annotation.JsonProperty
import io.billie.products.model.CountryDto
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("INVOICE_SUMMARY")
data class InvoiceRequestDto(
    val id: UUID,
    @JsonProperty("buyer_email")
    val buyer_id: String,
    val order_id: String,
    @JsonProperty("invoice_amount") val totalAmount: String,
    @JsonProperty("currency_code") val currencyCode: String,
    val country: CountryDto
)
/*
    id                 UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    buyer_id           VARCHAR(50) NOT NULL,  // We assume only email for consumer identification
    merchant_id        UUID   NOT NULL,
    order_id           VARCHAR(50),
    total_amount       FLOAT(10, 2) NOT NULL,
    currency_code      VARCHAR(3) NOT NULL,
    order_created      TIMESTAMP NOT NULL,
    order_updated      TIMESTAMP NOT NULL
 */