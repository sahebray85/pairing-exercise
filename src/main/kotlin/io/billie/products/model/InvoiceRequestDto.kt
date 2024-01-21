package io.billie.products.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class InvoiceRequestDto(
    val order_id: UUID,
    @JsonProperty("invoice_amount") val totalAmount: String,
    @JsonProperty("currency_code") val currencyCode: String,
)
