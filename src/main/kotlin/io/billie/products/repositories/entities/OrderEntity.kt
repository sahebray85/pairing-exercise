package io.billie.products.repositories.entities

import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table("ORDER_SUMMARY")
data class OrderEntity(
    val buyer_id: String,
    val merchant_id: UUID,
    val totalAmount: String,
    val currencyCode: String,
    val order_created: LocalDateTime
)
