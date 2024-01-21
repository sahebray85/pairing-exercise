package io.billie.products.controllers

import io.billie.products.exceptions.UnableToOrganisation
import io.billie.products.model.*
import io.billie.products.services.OrderService
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import javax.validation.Valid


@RestController
@RequestMapping("orders")
class OrderController(val service: OrderService) {

    @GetMapping
    fun index(): List<OrderSummaryDto> = service.findOrder()

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
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()])]
    )
    fun post(@Valid @RequestBody invoice: OrderRequestDto): Entity {
        try {
            val id = service.createOrder(invoice)
            return Entity(id)
        } catch (e: UnableToOrganisation) {
            throw ResponseStatusException(BAD_REQUEST, e.message)
        }
    }

}
