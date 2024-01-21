package io.billie.products.services

import io.billie.products.model.OrderRequestDto
import io.billie.products.model.OrderSummaryDto
import io.billie.products.repositories.OrderRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrderService(val db: OrderRepository) {

    fun findOrder(): List<OrderSummaryDto> = db.findInvoices()

    fun createOrder(order: OrderRequestDto): UUID {
        return db.create(order)
    }

}
