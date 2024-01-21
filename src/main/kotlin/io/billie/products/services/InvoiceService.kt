package io.billie.products.services

import io.billie.products.model.InvoiceRequestDto
import io.billie.products.model.OrderRequestDto
import io.billie.products.repositories.InvoiceRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class InvoiceService(val db: InvoiceRepository) {
   fun createInvoice(order: InvoiceRequestDto): UUID {
        return db.create(order)
    }
    fun updateInvoice(order: InvoiceRequestDto): UUID {
        return db.create(order)
    }

}
