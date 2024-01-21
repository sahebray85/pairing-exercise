package io.billie.products.repositories

import io.billie.products.enums.InvoiceStatusType
import io.billie.products.exceptions.UnableToFindOrder
import io.billie.products.model.*
import io.billie.products.repositories.entities.InvoiceEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*


@Repository
class InvoiceRepository {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Transactional
    fun create(invoice: InvoiceRequestDto): UUID {
        if(!validOrder(invoice.order_id)) {
            throw UnableToFindOrder(invoice.order_id)
        }
        return createInvoice(invoiceEntityMapper(invoice))
    }

    private fun validOrder(orderId: UUID): Boolean {
        val reply: String? = jdbcTemplate.query(
            "select id from organisations_schema.order_summary order_summary WHERE order_summary.id = ?",
            ResultSetExtractor {
                it.next()
                it.getString(1)
            },
            orderId
        )
        return (reply != null)
    }

    private fun invoiceEntityMapper(invoice: InvoiceRequestDto) : InvoiceEntity  {
        val currTime = LocalDateTime.now()
        return InvoiceEntity(invoice.order_id,
                invoice.totalAmount,
                invoice.currencyCode,
                currTime,
                InvoiceStatusType.INITIATED,
                currTime
            )
    }

    private fun createInvoice(invoice: InvoiceEntity): UUID {

        val keyHolder: KeyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(
            { connection ->
                val ps = connection.prepareStatement(
                    "INSERT INTO organisations_schema.invoice_summary (" +
                            "order_id, " +
                            "invoice_amount, " +
                            "currency_code, " +
                            "invoice_status, " +
                            "order_created, " +
                            "order_updated " +
                            ") VALUES (?, ?, ?, ?, ?, ?)",
                    arrayOf("id")
                )
                ps.setObject(1, invoice.orderId)
                ps.setBigDecimal(2, BigDecimal(invoice.amount))
                ps.setString(3, invoice.currencyCode)
                ps.setString(4, invoice.invoiceStatus.toString())
                ps.setTimestamp(5, Timestamp.valueOf(invoice.invoiceCreated))
                ps.setTimestamp(6, Timestamp.valueOf(invoice.invoiceUpdated))
                ps
            }, keyHolder
        )
        return keyHolder.getKeyAs(UUID::class.java)!!
    }

}
