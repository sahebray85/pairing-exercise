package io.billie.products.services

import io.billie.products.model.InvoiceRequestDto
import io.billie.products.model.InvoiceSummaryDto
import io.billie.products.model.OrganisationDto
import io.billie.products.model.OrganisationRequestDto
import io.billie.products.repositories.InvoiceRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class InvoiceService(val db: InvoiceRepository) {

    fun findInvoices(): List<InvoiceSummaryDto> = db .findInvoices()

    fun createInvoice(invoice: InvoiceRequestDto): UUID {
        return db.create(invoice)
    }

}
