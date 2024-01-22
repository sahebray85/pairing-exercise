package io.billie.products.services

import io.billie.products.model.PaymentRequestDto
import io.billie.products.repositories.PaymentRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class PaymentService(val db: PaymentRepository) {
   fun createPayment(payment: PaymentRequestDto): UUID {
        return db.create(payment)
    }

}
