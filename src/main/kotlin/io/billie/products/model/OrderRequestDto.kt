package io.billie.products.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import javax.validation.constraints.Size

data class OrderRequestDto(
    @JsonProperty("buyer_email")
    val buyerId: String,
    val merchantId: UUID,
    @JsonProperty("invoice_amount") val totalAmount: String,
    @JsonProperty("currency_code")  @Size(min = 3, max = 3) val currencyCode: String,
)
