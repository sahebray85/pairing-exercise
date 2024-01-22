package io.billie.products.controllers

import io.billie.products.exceptions.ImprorerCurrencyCodeException
import io.billie.products.exceptions.InvalidAmountException
import io.billie.products.exceptions.UnableToFindOrderException
import io.billie.products.model.Entity
import io.billie.products.model.PaymentRequestDto
import io.billie.products.services.PaymentService
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import javax.validation.Valid

@RestController
@RequestMapping("payments")
class PaymentController(val service: PaymentService) {

    @PostMapping
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Accepted the new invoice",
                content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = Entity::class)))
                    ))]
            ),
            ApiResponse(responseCode = "404", description = "Bad request", content = [Content()])]
    )
    fun post(@Valid @RequestBody payment: PaymentRequestDto): Entity {
        try {
            val id = service.createPayment(payment)
            return Entity(id)
        } catch (e: ImprorerCurrencyCodeException) {
            throw ResponseStatusException(BAD_REQUEST, e.message)
        } catch (e: InvalidAmountException) {
            throw ResponseStatusException(BAD_REQUEST, e.message)
        } catch (e: UnableToFindOrderException) {
            throw ResponseStatusException(BAD_REQUEST, e.message)
        }
    }

}
