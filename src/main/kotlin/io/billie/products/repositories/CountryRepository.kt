package io.billie.products.repositories

import io.billie.countries.model.CountryResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet
import java.util.*

@Repository
class CountryRepository {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Transactional(readOnly=true)
    fun findCountries(): List<CountryResponse> {
        val query = jdbcTemplate.query(
            "select id, name, country_code from organisations_schema.countries",
            countryResponseMapper()
        )
        return query
    }

    private fun countryResponseMapper() = RowMapper<CountryResponse> { it: ResultSet, _: Int ->
        CountryResponse(
            it.getObject("id", UUID::class.java),
            it.getString("name"),
            it.getString("country_code")
        )
    }
}
