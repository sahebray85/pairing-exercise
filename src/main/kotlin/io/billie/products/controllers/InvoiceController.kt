package io.billie.products.controllers

import io.billie.products.exceptions.UnableToFindCountry
import io.billie.products.services.OrganisationService
import io.billie.products.model.Entity
import io.billie.products.model.OrganisationRequestDto
import io.billie.products.model.OrganisationDto
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
@RequestMapping("invoices")
class OrganisationResource(val service: OrganisationService) {

    @GetMapping
    fun index(): List<OrganisationDto> = service.findOrganisations()

    @PostMapping
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Accepted the new organisation",
                content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = Entity::class)))
                    ))]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()])]
    )
    fun post(@Valid @RequestBody organisation: OrganisationRequestDto): Entity {
        try {
            val id = service.createOrganisation(organisation)
            return Entity(id)
        } catch (e: UnableToFindCountry) {
            throw ResponseStatusException(BAD_REQUEST, e.message)
        }
    }

}
