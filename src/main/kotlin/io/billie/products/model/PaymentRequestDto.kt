package io.billie.products.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import javax.validation.constraints.Size

data class PaymentRequestDto(
    @JsonProperty("invoice_id") val invoiceId: UUID,
    @JsonProperty("payment_amount") val amount: String,
    @JsonProperty("currency_code")  @Size(min = 3, max = 3) val currencyCode: String,
)
