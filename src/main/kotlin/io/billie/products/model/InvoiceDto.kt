package io.billie.products.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class InvoiceRequestDto(
    val id: UUID,
    @JsonProperty("buyer_email")
    val buyer_id: String,
    val merchant_id: String,
    val order_id: String,
    @JsonProperty("invoice_amount") val totalAmount: String,
    @JsonProperty("currency_code") val currencyCode: String,
)
