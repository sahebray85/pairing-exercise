package io.billie.functional

import com.fasterxml.jackson.databind.ObjectMapper
import io.billie.functional.data.Fixtures.invoicePayload
import io.billie.functional.data.Fixtures.invoiceWithOutOrderId
import io.billie.functional.data.Fixtures.orderPayload
import io.billie.functional.data.Fixtures.orgRequestJson
import io.billie.products.model.Entity
import org.hamcrest.MatcherAssert.assertThat
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
class InvoiceTest {

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
            post("/invoices").contentType(APPLICATION_JSON).content(invoiceWithOutOrderId())
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun createInvoice() {

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

        // Now create invoice
        val resultInvoice = mockMvc.perform(
            post("/invoices").contentType(APPLICATION_JSON).content(invoicePayload(responseOrders.id.toString()))
        )
            .andExpect(status().isOk)
            .andReturn()
        val responseInvoice = mapper.readValue(resultInvoice.response.contentAsString, Entity::class.java)


        assertThat("Invoice Id should not be null", responseInvoice.id != null)
    }

}
