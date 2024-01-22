package io.billie.products.repositories.entities

import io.billie.products.enums.InvoiceStatusType
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Table("INVOICE_SUMMARY")
data class InvoiceEntity(
    val invoiceId: UUID? = null,
    val orderId: UUID? = null,
    val amount: BigDecimal? = null,
    val currencyCode: String? = null,
    val invoiceCreated: LocalDateTime? = null,
    val invoiceStatus: InvoiceStatusType? = null,
    val invoiceUpdated: LocalDateTime? = null,
)
