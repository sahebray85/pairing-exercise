package io.billie.products.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import io.billie.products.enums.LegalEntityType
import java.time.LocalDate
import javax.validation.constraints.NotBlank

data class OrganisationRequestDto(
    @field:NotBlank val name: String,
    @JsonFormat(pattern = "dd/MM/yyyy") @JsonProperty("date_founded") val dateFounded: LocalDate,
    @field:NotBlank @JsonProperty("country_code") val countryCode: String,
    @JsonProperty("vat_number") val VATNumber: String?,
    @JsonProperty("registration_number") val registrationNumber: String?,
    @JsonProperty("local_address") val address: String?,
    @JsonProperty("legal_entity_type") val legalEntityType: LegalEntityType,
    @JsonProperty("contact_details") val contactDetails: ContactDetailsRequestDto,
)
