package io.billie.products.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import javax.validation.constraints.Size

data class CountryDto(
    val id: UUID,
    val name: String,
    @JsonProperty("country_code") @Size(min = 2, max = 2) val countryCode: String,
)
