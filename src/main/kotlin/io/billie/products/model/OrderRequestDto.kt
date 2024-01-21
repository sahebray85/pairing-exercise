package io.billie.products.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class OrderRequestDto(
    @JsonProperty("buyer_email")
    val buyer_id: String,
    val merchant_id: UUID,
    @JsonProperty("invoice_amount") val totalAmount: String,
    @JsonProperty("currency_code") val currencyCode: String,
)
