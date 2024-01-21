package io.billie.products.controllers

import io.billie.products.model.CountryDto
import io.billie.products.model.CityDto
import io.billie.products.services.CountryService
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("countries")
class LocationController(val service: CountryService) {

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "All countries",
                content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = CountryDto::class)))
                    ))]
            )]
    )
    @GetMapping
    fun index(): List<CountryDto> = service.findCountries()

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Found cities for country",
                content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = CityDto::class)))
                    ))]
            ),
            ApiResponse(responseCode = "404", description = "No cities found for country code", content = [Content()])]
    )
    @GetMapping("/{countryCode}/cities")
    fun cities(@PathVariable("countryCode") countryCode: String): List<CityDto> {
        val cities = service.findCities(countryCode.uppercase())
        if (cities.isEmpty()) {
            throw ResponseStatusException(
                NOT_FOUND,
                "No cities found for $countryCode"
            )
        }
        return cities
    }

}
