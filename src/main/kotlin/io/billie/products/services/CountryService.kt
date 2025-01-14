package io.billie.products.services

import io.billie.products.repositories.CityRepository
import io.billie.products.repositories.CountryRepository
import io.billie.products.model.CountryDto
import io.billie.products.model.CityDto
import io.billie.products.repositories.entities.CityEntity
import io.billie.products.repositories.entities.CountryEntity
import org.springframework.stereotype.Service

@Service
class CountryService(val dbCountry: CountryRepository, val dbCity: CityRepository, ) {

    fun findCountries(): List<CountryDto> = dbCountry.findCountries().map { countryResponseMapper(it) }

    fun findCities(countryCode: String): List<CityDto> = dbCity.findByCountryCode(countryCode).map { cityResponseMapper(it) }

    private fun countryResponseMapper(country: CountryEntity) = CountryDto(country.id, country.name, country.countryCode)
    private fun cityResponseMapper(city: CityEntity) = CityDto(city.id, city.name, city.countryCode);

}
