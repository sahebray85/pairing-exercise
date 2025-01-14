package io.billie.products.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import javax.validation.constraints.Size

data class OrderRequestDto(
    @JsonProperty("buyer_email")
    val buyerId: String,

    @JsonProperty("merchant_id")
    val merchantId: UUID,
    @JsonProperty("order_amount") val totalAmount: String,
    @JsonProperty("currency_code")  @Size(min = 3, max = 3) val currencyCode: String,
)
