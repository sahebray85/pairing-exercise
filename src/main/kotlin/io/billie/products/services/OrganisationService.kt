package io.billie.products.services

import io.billie.products.repositories.OrganisationRepository
import io.billie.products.model.OrganisationRequestDto
import io.billie.products.model.OrganisationDto
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrganisationService(val db: OrganisationRepository) {

    fun findOrganisations(): List<OrganisationDto> = db.findOrganisations()

    fun createOrganisation(organisation: OrganisationRequestDto): UUID {
        return db.create(organisation)
    }

}
