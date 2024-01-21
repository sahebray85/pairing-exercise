package io.billie.products.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import javax.validation.constraints.Size

data class CityDto(
    val id: UUID,
    val name: String,
    @Size(min = 2, max = 2)
    @JsonProperty("country_code")
    val countryCode: String
)
