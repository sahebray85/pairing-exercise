package io.billie.products.repositories

import io.billie.products.exceptions.UnableToFindCountry
import io.billie.products.enums.LegalEntityType
import io.billie.products.exceptions.UnableToOrganisation
import io.billie.products.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.Date
import java.sql.ResultSet
import java.util.*


@Repository
class InvoiceRepository {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Transactional(readOnly = true)
    fun findInvoices(): List<InvoiceSummaryDto> {
        return jdbcTemplate.query(invoiceQuery(), invoiceSummaryMapper())
    }

    @Transactional
    fun create(organisation: InvoiceRequestDto): UUID {
        if(!valuesValid(organisation)) {
            throw UnableToFindCountry(organisation.countryCode)
        }
        val id: UUID = createContactDetails(organisation.contactDetails)
        return createInvoice(organisation, id)
    }

    private fun valuesValid(invoice: InvoiceSummaryDto): Boolean {
        val reply: Int? = jdbcTemplate.query(
            "select count(country_code) from organisations_schema.countries c WHERE c.country_code = ?",
            ResultSetExtractor {
                it.next()
                it.getInt(1)
            },
            // invoice.countryCode
        )
        return (reply != null) && (reply > 0)
    }

    private fun validOrganisation(orgId: String): Boolean {
        val reply: Int? = jdbcTemplate.query(
            "select id from organisations_schema.organization org WHERE org.id = ?",
            ResultSetExtractor {
                it.next()
                it.getInt(1)
            },
            // invoice.countryCode
        )
        return (reply != null) && (reply > 0)
    }

    private fun createInvoice(invoice: InvoiceRequestDto): UUID {
        if(!validOrganisation(invoice.merchant_id)) {
            throw UnableToOrganisation(invoice.merchant_id)
        }
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(
            { connection ->
                val ps = connection.prepareStatement(
                    "INSERT INTO organisations_schema.invoice_summary (" +
                            "name, " +
                            "date_founded, " +
                            "country_code, " +
                            "vat_number, " +
                            "registration_number, " +
                            "local_address, " +
                            "legal_entity_type, " +
                            "contact_details_id" +
                            ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    arrayOf("id")
                )
                ps.setString(1, org.name)
                ps.setDate(2, Date.valueOf(org.dateFounded))
                ps.setString(3, org.countryCode)
                ps.setString(4, org.VATNumber)
                ps.setString(5, org.registrationNumber)
                ps.setString(6, org.address)
                ps.setString(7, org.legalEntityType.toString())
                ps.setObject(8, contactDetailsId)
                ps
            }, keyHolder
        )
        return keyHolder.getKeyAs(UUID::class.java)!!
    }

    private fun createContactDetails(contactDetails: ContactDetailsRequestDto): UUID {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(
            { connection ->
                val ps = connection.prepareStatement(
                    "insert into organisations_schema.contact_details " +
                            "(" +
                            "phone_number, " +
                            "fax, " +
                            "email" +
                            ") values(?,?,?)",
                    arrayOf("id")
                )
                ps.setString(1, contactDetails.phoneNumber)
                ps.setString(2, contactDetails.fax)
                ps.setString(3, contactDetails.email)
                ps
            },
            keyHolder
        )
        return keyHolder.getKeyAs(UUID::class.java)!!
    }

    private fun invoiceQuery() = "select " +
            "o.id as id, " +
            "o.name as name, " +
            "o.date_founded as date_founded, " +
            "o.country_code as country_code, " +
            "c.id as country_id, " +
            "c.name as country_name, " +
            "o.VAT_number as VAT_number, " +
            "o.registration_number as registration_number," +
            "o.local_address as local_address," +
            "o.legal_entity_type as legal_entity_type," +
            "o.contact_details_id as contact_details_id, " +
            "cd.phone_number as phone_number, " +
            "cd.fax as fax, " +
            "cd.email as email " +
            "from " +
            "organisations_schema.organisations o " +
            "INNER JOIN organisations_schema.contact_details cd on o.contact_details_id::uuid = cd.id::uuid " +
            "INNER JOIN organisations_schema.countries c on o.country_code = c.country_code "

    private fun organisationMapper() = RowMapper<OrganisationDto> { it: ResultSet, _: Int ->
        OrganisationDto(
            it.getObject("id", UUID::class.java),
            it.getString("name"),
            Date(it.getDate("date_founded").time).toLocalDate(),
            mapCountry(it),
            it.getString("vat_number"),
            it.getString("registration_number"),
            it.getString("local_address"),
            LegalEntityType.valueOf(it.getString("legal_entity_type")),
            mapContactDetails(it)
        )
    }

    private fun mapContactDetails(it: ResultSet): ContactDetailsRequestDto {
        return ContactDetailsRequestDto(
            // UUID.fromString(it.getString("contact_details_id")),
            it.getString("phone_number"),
            it.getString("fax"),
            it.getString("email")
        )
    }

    private fun mapCountry(it: ResultSet): CountryDto {
        return CountryDto(
            it.getObject("country_id", UUID::class.java),
            it.getString("country_name"),
            it.getString("country_code")
        )
    }

}
