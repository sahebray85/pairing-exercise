package io.billie.products.repositories

import io.billie.products.exceptions.UnableToFindOrganisation
import io.billie.products.model.OrderRequestDto
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
class OrderRepository {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Transactional
    fun create(order: OrderRequestDto): UUID {
        if(!validOrganisation(order.merchant_id)) {
            throw UnableToFindOrganisation(order.merchant_id)
        }
        return createOrder(orderEntityMapper(order))
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

    private fun orderEntityMapper(order: OrderRequestDto) : OrderEntity  {
        val currTime = LocalDateTime.now()
        return OrderEntity(order.buyer_id,
                order.merchant_id,
                BigDecimal(order.totalAmount),
                order.currencyCode,
                currTime
            )
    }

    private fun createOrder(order: OrderEntity): UUID {

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
                ps.setString(1, order.buyerId)
                ps.setObject(2, order.merchantId)
                ps.setBigDecimal(3, order.totalAmount)
                ps.setString(4, order.currencyCode)
                ps.setTimestamp(5, Timestamp.valueOf(order.orderCreated))
                ps
            }, keyHolder
        )
        return keyHolder.getKeyAs(UUID::class.java)!!
    }

}
