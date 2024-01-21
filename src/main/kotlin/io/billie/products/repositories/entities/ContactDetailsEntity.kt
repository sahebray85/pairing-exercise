package io.billie.products.repositories.entities

import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("contact_details")
data class ContactDetailsEntity(
    val id: UUID?,
    val phoneNumber: String?,
    val fax: String?,
    val email: String?
)
