package io.billie.products.repositories.entities

import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Table("ORDER_SUMMARY")
data class OrderEntity(
    val buyerId: String? = null,
    val merchantId: UUID? = null ,
    val totalAmount: BigDecimal? = null,
    val currencyCode: String? = null,
    val orderCreated: LocalDateTime? = null
)
