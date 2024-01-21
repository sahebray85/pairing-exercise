package io.billie.products.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class OrderSummaryDto(
    val id: UUID,
    @JsonProperty("buyer_email")
    val buyer_id: String,
    @JsonProperty("merchant_details")
    val merchant_details: OrganisationDto,
    val order_id: String,
    @JsonProperty("invoice_amount") val totalAmount: String,
    @JsonProperty("currency_code") val currencyCode: String,
    @JsonProperty("invoice_created") val invoiceCreated: String,
    @JsonProperty("invoice_updated") val invoiceUpdated: String
)
