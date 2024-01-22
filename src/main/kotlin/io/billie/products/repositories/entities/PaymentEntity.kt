package io.billie.products.repositories.entities

import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table("PAYMENT_SUMMARY")
data class PaymentEntity(
    val paymentId: UUID? = null,
    val invoiceId: UUID,
    val amount: String,
    val currencyCode: String,
    val paymentDate: LocalDateTime
)
