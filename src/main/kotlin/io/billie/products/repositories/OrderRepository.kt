package io.billie.products.repositories

import io.billie.products.exceptions.UnableToOrganisation
import io.billie.products.model.ContactDetailsRequestDto
import io.billie.products.model.CountryDto
import io.billie.products.model.OrderRequestDto
import io.billie.products.model.OrderSummaryDto
import io.billie.products.repositories.entities.InvoiceEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.sql.ResultSet
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*


@Repository
class OrderRepository {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Transactional(readOnly = true)
    fun findInvoices(): List<OrderSummaryDto> {
        return ArrayList()
    }

    @Transactional
    fun create(order: OrderRequestDto): UUID {
        if(!validOrganisation(order.merchant_id)) {
            throw UnableToOrganisation(order.merchant_id)
        }
        return createInvoice(orderEntityMapper(order))
    }

    private fun validOrganisation(orgId: UUID): Boolean {
        val reply: String? = jdbcTemplate.query(
            "select id from organisations_schema.organisations org WHERE org.id = ?",
            ResultSetExtractor {
                it.next()
                it.getString(1)
            },
            orgId
        )
        return (reply != null)
    }

    private fun orderEntityMapper(invoice: OrderRequestDto) : InvoiceEntity  {
        val currTime = LocalDateTime.now()
        return InvoiceEntity(invoice.buyer_id,
                invoice.merchant_id,
                invoice.totalAmount,
                invoice.currencyCode,
                currTime
            )
    }

    private fun createInvoice(invoice: InvoiceEntity): UUID {

        val keyHolder: KeyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(
            { connection ->
                val ps = connection.prepareStatement(
                    "INSERT INTO organisations_schema.order_summary (" +
                            "buyer_id, " +
                            "merchant_id, " +
                            "total_amount, " +
                            "currency_code, " +
                            "order_created " +
                            ") VALUES (?, ?, ?, ?, ?)",
                    arrayOf("id")
                )
                ps.setString(1, invoice.buyer_id)
                ps.setObject(2, invoice.merchant_id)
                ps.setBigDecimal(3, BigDecimal(invoice.totalAmount))
                ps.setString(4, invoice.currencyCode)
                ps.setTimestamp(5, Timestamp.valueOf(invoice.order_created))
                ps
            }, keyHolder
        )
        return keyHolder.getKeyAs(UUID::class.java)!!
    }

}
