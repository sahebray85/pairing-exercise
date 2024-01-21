package io.billie.products.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import io.billie.products.enums.LegalEntityType
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.util.*

@Table("ORGANISATIONS")
data class OrganisationDto(
    val id: UUID,
    val name: String,
    @JsonFormat(pattern = "dd/MM/yyyy") @JsonProperty("date_founded") val dateFounded: LocalDate,
    val country: CountryDto,
    @JsonProperty("vat_number") val VATNumber: String?,
    @JsonProperty("registration_number") val registrationNumber: String?,
    @JsonProperty("local_address") val address: String?,
    @JsonProperty("legal_entity_type") val legalEntityType: LegalEntityType,
    @JsonProperty("contact_details") val contactDetails: ContactDetailsRequestDto,
)
