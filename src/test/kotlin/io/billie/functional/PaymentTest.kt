package io.billie.functional

import com.fasterxml.jackson.databind.ObjectMapper
import io.billie.functional.data.Fixtures
import io.billie.functional.data.Fixtures.invoicePayload
import io.billie.functional.data.Fixtures.invoicePayloadWithAmount
import io.billie.functional.data.Fixtures.invoiceWithOutOrderId
import io.billie.functional.data.Fixtures.orderPayload
import io.billie.functional.data.Fixtures.orgRequestJson
import io.billie.functional.data.Fixtures.paymentPayload
import io.billie.functional.data.Fixtures.paymentPayloadWoInvoiceId
import io.billie.products.enums.InvoiceStatusType
import io.billie.products.model.Entity
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = DEFINED_PORT)
class PaymentTest {

    @LocalServerPort
    private val port = 8080

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Autowired
    private lateinit var template: JdbcTemplate

    @Test
    fun placeOrderWithNoMerchant() {
        mockMvc.perform(
            post("/payments").contentType(APPLICATION_JSON).content(paymentPayloadWoInvoiceId())
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun testPaymentHappyPath() {

        // Perform org call to get org id
        val resultOrg = mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestJson())
        )
            .andExpect(status().isOk)
            .andReturn()

        val responseOrg = mapper.readValue(resultOrg.response.contentAsString, Entity::class.java)
        val orgId = responseOrg.id

        // Place an order
        val resultOrders = mockMvc.perform(
            post("/orders").contentType(APPLICATION_JSON).content(orderPayload(orgId.toString()))
        )
            .andExpect(status().isOk)
            .andReturn()
        val responseOrders = mapper.readValue(resultOrders.response.contentAsString, Entity::class.java)

        val amount = "1200.30"
        val currencyCode = "EUR"
        // Now create invoice
        val resultInvoice = mockMvc.perform(
            post("/invoices").contentType(APPLICATION_JSON).content(
                invoicePayloadWithAmount(
                    responseOrders.id.toString(),
                    amount,
                    currencyCode
                )))
            .andExpect(status().isOk)
            .andReturn()
        val responseInvoices = mapper.readValue(resultInvoice.response.contentAsString, Entity::class.java)

        // Now payment
        val resultPayments = mockMvc.perform(
            post("/payments").contentType(APPLICATION_JSON).content(
                paymentPayload(responseInvoices.id.toString(), amount, currencyCode)
        ))
            .andExpect(status().isOk)
            .andReturn()
        val responsePayments = mapper.readValue(resultPayments.response.contentAsString, Entity::class.java)
        assertThat("Payment Id should not be null", responsePayments.id != null)

        // Check invoice id as COMPLETED
        assertDataMatches(invoiceFromDatabase(responseInvoices.id), Fixtures.invoiceStatusFixture(InvoiceStatusType.COMPLETED))
    }

    @Test
    fun testRePaymentWithSameInvoiceId() {

        // Perform org call to get org id
        val resultOrg = mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestJson())
        )
            .andExpect(status().isOk)
            .andReturn()

        val responseOrg = mapper.readValue(resultOrg.response.contentAsString, Entity::class.java)
        val orgId = responseOrg.id

        // Place an order
        val resultOrders = mockMvc.perform(
            post("/orders").contentType(APPLICATION_JSON).content(orderPayload(orgId.toString()))
        )
            .andExpect(status().isOk)
            .andReturn()
        val responseOrders = mapper.readValue(resultOrders.response.contentAsString, Entity::class.java)

        val amount = "1200.30"
        val currencyCode = "EUR"
        // Now create invoice
        val resultInvoice = mockMvc.perform(
            post("/invoices").contentType(APPLICATION_JSON).content(
                invoicePayloadWithAmount(
                    responseOrders.id.toString(),
                    amount,
                    currencyCode
                )))
            .andExpect(status().isOk)
            .andReturn()
        val responseInvoices = mapper.readValue(resultInvoice.response.contentAsString, Entity::class.java)

        // Now payment
        val resultPayments = mockMvc.perform(
            post("/payments").contentType(APPLICATION_JSON).content(
                paymentPayload(responseInvoices.id.toString(), amount, currencyCode)
            ))
            .andExpect(status().isOk)
            .andReturn()
        val responsePayments = mapper.readValue(resultPayments.response.contentAsString, Entity::class.java)
        assertThat("Payment Id should not be null", responsePayments.id != null)

        // Check invoice id as COMPLETED
        assertDataMatches(invoiceFromDatabase(responseInvoices.id), Fixtures.invoiceStatusFixture(InvoiceStatusType.COMPLETED))

        // Repayment
        mockMvc.perform(
            post("/payments").contentType(APPLICATION_JSON).content(
                paymentPayload(responseInvoices.id.toString(), amount, currencyCode)
            ))
            .andExpect(status().isBadRequest)
    }


    @Test
    fun testOverPayment() {

        // Perform org call to get org id
        val resultOrg = mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestJson())
        )
            .andExpect(status().isOk)
            .andReturn()

        val responseOrg = mapper.readValue(resultOrg.response.contentAsString, Entity::class.java)
        val orgId = responseOrg.id

        // Place an order
        val resultOrders = mockMvc.perform(
            post("/orders").contentType(APPLICATION_JSON).content(orderPayload(orgId.toString()))
        )
            .andExpect(status().isOk)
            .andReturn()
        val responseOrders = mapper.readValue(resultOrders.response.contentAsString, Entity::class.java)

        val amount = "1200.30"
        val currencyCode = "EUR"
        // Now create invoice
        val resultInvoice = mockMvc.perform(
            post("/invoices").contentType(APPLICATION_JSON).content(
                invoicePayloadWithAmount(
                    responseOrders.id.toString(),
                    amount,
                    currencyCode
                )))
            .andExpect(status().isOk)
            .andReturn()
        val responseInvoices = mapper.readValue(resultInvoice.response.contentAsString, Entity::class.java)

        // Test Over payment
        mockMvc.perform(
            post("/payments").contentType(APPLICATION_JSON).content(
                paymentPayload(responseInvoices.id.toString(), "50000.00", currencyCode)
            ))
            .andExpect(status().isBadRequest)
            .andReturn()
    }


    fun assertDataMatches(reply: Map<String, Any>, assertions: Map<String, Any>) {
        for (key in assertions.keys) {
            assertThat(reply[key], IsEqual.equalTo(assertions[key]))
        }
    }
    private fun queryEntityFromDatabase(sql: String, id: UUID): MutableMap<String, Any> =
        template.queryForMap(sql, id)
    private fun invoiceFromDatabase(id: UUID): MutableMap<String, Any> =
        queryEntityFromDatabase("select invoice_status from organisations_schema.invoice_summary where id = ?", id)

}
