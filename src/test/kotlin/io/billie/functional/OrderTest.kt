package io.billie.functional

import com.fasterxml.jackson.databind.ObjectMapper
import io.billie.functional.data.Fixtures.bbcContactFixture
import io.billie.functional.data.Fixtures.bbcFixture
import io.billie.functional.data.Fixtures.orderPayload
import io.billie.functional.data.Fixtures.orderWitNoBuyer
import io.billie.functional.data.Fixtures.orderWithEmptyMerchantId
import io.billie.functional.data.Fixtures.orgRequestJson
import io.billie.functional.data.Fixtures.orgRequestJsonCountryCodeBlank
import io.billie.functional.data.Fixtures.orgRequestJsonCountryCodeIncorrect
import io.billie.functional.data.Fixtures.orgRequestJsonNoName
import io.billie.functional.data.Fixtures.orgRequestJsonNameBlank
import io.billie.functional.data.Fixtures.orgRequestJsonNoContactDetails
import io.billie.functional.data.Fixtures.orgRequestJsonNoCountryCode
import io.billie.functional.data.Fixtures.orgRequestJsonNoLegalEntityType
import io.billie.products.model.Entity
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = DEFINED_PORT)
class OrderTest {

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
            post("/orders").contentType(APPLICATION_JSON).content(orderWithEmptyMerchantId())
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun placeOrderWithNoBuyer() {
        mockMvc.perform(
            post("/orders").contentType(APPLICATION_JSON).content(orderWitNoBuyer())
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun placeOrder() {

        // Perform org call to get org id
        val resultOrg = mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestJson())
        )
            .andExpect(status().isOk)
            .andReturn()

        val responseOrg = mapper.readValue(resultOrg.response.contentAsString, Entity::class.java)
        val orgId = responseOrg.id

        val resultOrders = mockMvc.perform(
            post("/orders").contentType(APPLICATION_JSON).content(orderPayload(orgId.toString()))
        )
            .andExpect(status().isOk)
        val responseOrders = mapper.readValue(resultOrg.response.contentAsString, Entity::class.java)
        assertThat("Order Id should not be null", responseOrders.id != null)
    }

}
