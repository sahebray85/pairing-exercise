package io.billie.products.repositories.entities

import io.billie.products.enums.InvoiceStatusType
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table("INVOICE_SUMMARY")
data class InvoiceEntity(
    //val invoice_id: UUID?,
    val orderId: UUID,
    val amount: String,
    val currencyCode: String,
    val invoiceCreated: LocalDateTime,
    val invoiceStatus: InvoiceStatusType,
    val invoiceUpdated: LocalDateTime,
)
