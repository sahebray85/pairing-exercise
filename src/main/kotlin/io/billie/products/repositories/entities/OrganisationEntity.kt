package io.billie.products.repositories.entities

import io.billie.products.enums.LegalEntityType
import io.billie.products.model.ContactDetailsDto
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table("ORGANISATIONS")
data class OrganisationEntity(
    val name: String,
    val dateFounded: LocalDate,
    val countryCode: String,
    val VATNumber: String?,
    val registrationNumber: String?,
    val address: String?,
    val legalEntityType: LegalEntityType,
    val contactDetails: ContactDetailsDto,
)
