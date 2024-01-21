package io.billie.products.repositories.entities

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import javax.validation.constraints.Size

data class CityEntity(
    val id: UUID,
    val name: String,
    val countryCode: String
)
