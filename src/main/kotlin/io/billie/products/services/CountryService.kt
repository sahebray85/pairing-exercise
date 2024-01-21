package io.billie.products.services

import io.billie.products.repositories.CityRepository
import io.billie.products.repositories.CountryRepository
import io.billie.countries.model.CityResponse
import io.billie.countries.model.CountryResponse
import org.springframework.stereotype.Service

@Service
class CountryService(val dbCountry: CountryRepository, val dbCity: CityRepository, ) {

    fun findCountries(): List<CountryResponse> {
        return dbCountry.findCountries()
    }
    fun findCities(countryCode: String): List<CityResponse> = dbCity.findByCountryCode(countryCode)

}