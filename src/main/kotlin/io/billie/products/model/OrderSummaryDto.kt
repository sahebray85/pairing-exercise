package io.billie.products.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class OrderSummaryDto(
    val id: UUID,
    @JsonProperty("buyer_email")
    val buyerId: String,
    @JsonProperty("merchant_details")
    val merchantDetails: OrganisationDto,
    @JsonProperty("invoice_amount") val totalAmount: String,
    @JsonProperty("currency_code") val currencyCode: String,
    @JsonProperty("invoice_created") val invoiceCreated: String,
    @JsonProperty("invoice_updated") val invoiceUpdated: String
)
