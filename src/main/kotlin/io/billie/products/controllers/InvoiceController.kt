package io.billie.products.controllers

import io.billie.products.exceptions.ImprorerCurrencyCodeException
import io.billie.products.exceptions.InvalidInvoiceAmountException
import io.billie.products.exceptions.UnableToFindOrderException
import io.billie.products.model.Entity
import io.billie.products.model.InvoiceRequestDto
import io.billie.products.services.InvoiceService
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
@RequestMapping("invoices")
class InvoiceController(val service: InvoiceService) {

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
    fun post(@Valid @RequestBody invoice: InvoiceRequestDto): Entity {
        try {
            val id = service.createInvoice(invoice)
            return Entity(id)
        } catch (e: ImprorerCurrencyCodeException) {
            throw ResponseStatusException(BAD_REQUEST, e.message)
        } catch (e: InvalidInvoiceAmountException) {
            throw ResponseStatusException(BAD_REQUEST, e.message)
        } catch (e: UnableToFindOrderException) {
            throw ResponseStatusException(BAD_REQUEST, e.message)
        }
    }

}
