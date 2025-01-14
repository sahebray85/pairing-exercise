package io.billie.products.repositories.entities

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.relational.core.mapping.Table
import java.util.*
import javax.validation.constraints.Size
data class CountryEntity(
    val id: UUID,
    val name: String,
    val countryCode: String,
)
