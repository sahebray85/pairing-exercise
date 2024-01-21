package io.billie.products.repositories

import io.billie.products.enums.InvoiceStatusType
import io.billie.products.exceptions.InvalidInvoiceAmount
import io.billie.products.exceptions.UnableToFindOrder
import io.billie.products.model.*
import io.billie.products.repositories.entities.InvoiceEntity
import io.billie.products.repositories.entities.OrderEntity
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

        val order = getOrderDetails(invoice.order_id) ?: throw UnableToFindOrder(invoice.order_id)

        val orderAmount = order.totalAmount ?: BigDecimal.ZERO;
        val totalInvoiceAmount = getTotalInvoiceAmount(invoice.order_id);
        val requestedInvoiceAmount = BigDecimal(invoice.totalAmount);
        if(requestedInvoiceAmount > orderAmount) {
            throw InvalidInvoiceAmount("Invoice amount {} is greater than order amount {}".format(totalInvoiceAmount, orderAmount))
        }
        if(requestedInvoiceAmount + totalInvoiceAmount > orderAmount) {
            val pendingAmount = orderAmount - totalInvoiceAmount
            throw InvalidInvoiceAmount("Invoice amount {} can't exceed {} for a total order amount of {}".format(requestedInvoiceAmount, pendingAmount, orderAmount))
        }
        return createInvoice(invoiceEntityMapper(invoice))
    }

    fun getOrderDetails(orderId: UUID) : OrderEntity? {
        val sql = "select * from organisations_schema.order_summary order_summary WHERE order_summary.id = ?"
        val reply: List<OrderEntity> = jdbcTemplate.query(
            sql,
            { rs, _ ->
                OrderEntity(
                    totalAmount = rs.getBigDecimal("total_amount"),
                    currencyCode = rs.getString("currency_code")
                )
            },
            orderId
        )
        return reply.first()
    }

    fun getTotalInvoiceAmount(orderId: UUID) : BigDecimal {

        val sql = "select sum(invoice.invoice_amount) from organisations_schema.invoice_summary invoice WHERE invoice.order_id = ? GROUP BY invoice.order_id"
        val reply: BigDecimal? = jdbcTemplate.query(
            sql,
            ResultSetExtractor {
                it.next()
                it.getBigDecimal(1)
            },
            orderId
        )
        return reply ?: BigDecimal.ZERO
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
