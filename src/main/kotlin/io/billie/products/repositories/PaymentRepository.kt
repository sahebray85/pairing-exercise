package io.billie.products.repositories

import io.billie.products.enums.InvoiceStatusType
import io.billie.products.exceptions.ImprorerCurrencyCodeException
import io.billie.products.exceptions.InvalidAmountException
import io.billie.products.exceptions.UnableToFindInvoiceException
import io.billie.products.model.*
import io.billie.products.repositories.entities.InvoiceEntity
import io.billie.products.repositories.entities.PaymentEntity
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
class PaymentRepository {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Transactional
    fun create(payment: PaymentRequestDto): UUID {

        val invoice = getInvoiceDetails(payment.invoiceId) ?: throw UnableToFindInvoiceException(payment.invoiceId)

        if(BigDecimal(payment.amount) != invoice.amount ) {
            throw InvalidAmountException("Payment amount (${payment.amount}) must be same as invoice amount (${invoice.amount})")
        }

        if (payment.currencyCode != invoice.currencyCode) {
            throw ImprorerCurrencyCodeException("Payment currency code ${payment.currencyCode} should match invoice currency code ${invoice.currencyCode}")
        }

        if (invoice.invoiceStatus == InvoiceStatusType.COMPLETED) {
            throw ImprorerCurrencyCodeException("Invoice Id already completed ${invoice.invoiceId}")
        }

        // Update Invoice & Create Payment. These two operations should be a Single Transaction.
        updateInvoice(payment.invoiceId)
        return createPayment(paymentEntityMapper(payment))
    }

    fun getInvoiceDetails(invoiceId: UUID) : InvoiceEntity? {
        val sql = "select * from organisations_schema.invoice_summary invoice WHERE invoice.id = ? FOR UPDATE"
        val reply: List<InvoiceEntity> = jdbcTemplate.query(
            sql,
            { rs, _ ->
                InvoiceEntity(
                    amount = rs.getBigDecimal("invoice_amount"),
                    currencyCode = rs.getString("currency_code"),
                    invoiceStatus = InvoiceStatusType.valueOf(rs.getString("invoice_status"))
                )
            },
            invoiceId
        )
        return reply.first()
    }

    fun updateInvoice(invoiceId: UUID) {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        val currTime = LocalDateTime.now()
        jdbcTemplate.update(
            { connection ->
                val ps = connection.prepareStatement(
                    "UPDATE organisations_schema.invoice_summary set invoice_status = ?, invoice_updated = ? where id = ?",
                    arrayOf("id")
                )
                ps.setString(1, InvoiceStatusType.COMPLETED.toString())
                ps.setTimestamp(2, Timestamp.valueOf(currTime))
                ps.setObject(3, invoiceId)
                ps
            }, keyHolder
        )
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

    private fun paymentEntityMapper(payment: PaymentRequestDto) : PaymentEntity  {
        val currTime = LocalDateTime.now()
        return PaymentEntity(
                invoiceId = payment.invoiceId,
                amount = payment.amount,
                currencyCode = payment.currencyCode,
                paymentDate = currTime
            )
    }

    private fun createPayment(payment: PaymentEntity): UUID {

        val keyHolder: KeyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(
            { connection ->
                val ps = connection.prepareStatement(
                    "INSERT INTO organisations_schema.payment_summary (" +
                            "invoice_id, " +
                            "payment_amount, " +
                            "currency_code, " +
                            "payment_date " +
                            ") VALUES (?, ?, ?, ?)",
                    arrayOf("id")
                )
                ps.setObject(1, payment.invoiceId)
                ps.setBigDecimal(2, BigDecimal(payment.amount))
                ps.setString(3, payment.currencyCode)
                ps.setTimestamp(4, Timestamp.valueOf(payment.paymentDate))
                ps
            }, keyHolder
        )
        return keyHolder.getKeyAs(UUID::class.java)!!
    }

}
