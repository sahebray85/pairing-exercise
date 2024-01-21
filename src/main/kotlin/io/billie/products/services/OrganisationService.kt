package io.billie.products.services

import io.billie.products.repositories.OrganisationRepository
import io.billie.products.model.OrganisationDto
import io.billie.organisations.viewmodel.OrganisationResponse
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrganisationService(val db: OrganisationRepository) {

    fun findOrganisations(): List<OrganisationResponse> = db.findOrganisations()

    fun createOrganisation(organisation: OrganisationDto): UUID {
        return db.create(organisation)
    }

}
