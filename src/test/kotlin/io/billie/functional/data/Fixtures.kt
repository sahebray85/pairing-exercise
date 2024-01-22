package io.billie.functional.data

import io.billie.products.enums.InvoiceStatusType
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

object Fixtures {

    fun orgRequestJsonNameBlank(): String {
        return "{\n" +
                "  \"name\": \"\",\n" +
                "  \"date_founded\": \"18/10/1922\",\n" +
                "  \"country_code\": \"GB\",\n" +
                "  \"vat_number\": \"333289454\",\n" +
                "  \"registration_number\": \"3686147\",\n" +
                "  \"legal_entity_type\": \"NONPROFIT_ORGANIZATION\",\n" +
                "  \"contact_details\": {\n" +
                "    \"phone_number\": \"+443700100222\",\n" +
                "    \"fax\": \"\",\n" +
                "    \"email\": \"yourquestions@bbc.co.uk\"\n" +
                "  }\n" +
                "}"
    }

    fun orgRequestJsonNoName(): String {
        return "{\n" +
                "  \"date_founded\": \"18/10/1922\",\n" +
                "  \"country_code\": \"GB\",\n" +
                "  \"vat_number\": \"333289454\",\n" +
                "  \"registration_number\": \"3686147\",\n" +
                "  \"local_address\": \"some address\", \n" +
                "  \"legal_entity_type\": \"NONPROFIT_ORGANIZATION\",\n" +
                "  \"contact_details\": {\n" +
                "    \"phone_number\": \"+443700100222\",\n" +
                "    \"fax\": \"\",\n" +
                "    \"email\": \"yourquestions@bbc.co.uk\"\n" +
                "  }\n" +
                "}"
    }

    fun orgRequestJsonNoLegalEntityType(): String {
        return "{\n" +
                "  \"name\": \"BBC\",\n" +
                "  \"date_founded\": \"18/10/1922\",\n" +
                "  \"country_code\": \"GB\",\n" +
                "  \"vat_number\": \"333289454\",\n" +
                "  \"registration_number\": \"3686147\",\n" +
                "  \"local_address\": \"some address\", \n" +
                "  \"contact_details\": {\n" +
                "    \"phone_number\": \"+443700100222\",\n" +
                "    \"fax\": \"\",\n" +
                "    \"email\": \"yourquestions@bbc.co.uk\"\n" +
                "  }\n" +
                "}"
    }

    fun orgRequestJsonNoContactDetails(): String {
        return "{\n" +
                "  \"name\": \"BBC\",\n" +
                "  \"date_founded\": \"18/10/1922\",\n" +
                "  \"country_code\": \"GB\",\n" +
                "  \"vat_number\": \"333289454\",\n" +
                "  \"registration_number\": \"3686147\",\n" +
                "  \"local_address\": \"some address\", \n" +
                "  \"legal_entity_type\": \"NONPROFIT_ORGANIZATION\"\n" +
                "}"
    }

    fun orgRequestJson(): String {
        return "{\n" +
                "  \"name\": \"BBC\",\n" +
                "  \"date_founded\": \"18/10/1922\",\n" +
                "  \"country_code\": \"GB\",\n" +
                "  \"vat_number\": \"333289454\",\n" +
                "  \"registration_number\": \"3686147\",\n" +
                "  \"local_address\": \"some address\", \n" +
                "  \"legal_entity_type\": \"NONPROFIT_ORGANIZATION\",\n" +
                "  \"contact_details\": {\n" +
                "    \"phone_number\": \"+443700100222\",\n" +
                "    \"fax\": \"\",\n" +
                "    \"email\": \"yourquestions@bbc.co.uk\"\n" +
                "  }\n" +
                "}"
    }

    fun orgRequestJsonCountryCodeBlank(): String {
        return "{\n" +
                "  \"name\": \"BBC\",\n" +
                "  \"date_founded\": \"18/10/1922\",\n" +
                "  \"country_code\": \"\",\n" +
                "  \"vat_number\": \"333289454\",\n" +
                "  \"registration_number\": \"3686147\",\n" +
                "  \"local_address\": \"some address\", \n" +
                "  \"legal_entity_type\": \"NONPROFIT_ORGANIZATION\",\n" +
                "  \"contact_details\": {\n" +
                "    \"phone_number\": \"+443700100222\",\n" +
                "    \"fax\": \"\",\n" +
                "    \"email\": \"yourquestions@bbc.co.uk\"\n" +
                "  }\n" +
                "}"
    }

    fun orgRequestJsonNoCountryCode(): String {
        return "{\n" +
                "  \"name\": \"BBC\",\n" +
                "  \"date_founded\": \"18/10/1922\",\n" +
                "  \"vat_number\": \"333289454\",\n" +
                "  \"registration_number\": \"3686147\",\n" +
                "  \"legal_entity_type\": \"NONPROFIT_ORGANIZATION\",\n" +
                "  \"local_address\": \"some address\", \n" +
                "  \"contact_details\": {\n" +
                "    \"phone_number\": \"+443700100222\",\n" +
                "    \"fax\": \"\",\n" +
                "    \"email\": \"yourquestions@bbc.co.uk\"\n" +
                "  }\n" +
                "}"
    }

    fun orgRequestJsonCountryCodeIncorrect(): String {
        return "{\n" +
                "  \"name\": \"BBC\",\n" +
                "  \"date_founded\": \"18/10/1922\",\n" +
                "  \"country_code\": \"XX\",\n" +
                "  \"vat_number\": \"333289454\",\n" +
                "  \"registration_number\": \"3686147\",\n" +
                "  \"local_address\": \"some address\", \n" +
                "  \"legal_entity_type\": \"NONPROFIT_ORGANIZATION\",\n" +
                "  \"contact_details\": {\n" +
                "    \"phone_number\": \"+443700100222\",\n" +
                "    \"fax\": \"\",\n" +
                "    \"email\": \"yourquestions@bbc.co.uk\"\n" +
                "  }\n" +
                "}"
    }

    fun bbcFixture(id: UUID): Map<String, Any> {
        val data = HashMap<String, Any>()
        data["id"] = id
        data["name"] = "BBC"
        data["date_founded"] = SimpleDateFormat("yyyy-MM-dd").parse("1922-10-18")
        data["country_code"] = "GB"
        data["vat_number"] = "333289454"
        data["registration_number"] = "3686147"
        data["legal_entity_type"] = "NONPROFIT_ORGANIZATION"
        data["local_address"] = "some address"
        return data
    }

    fun bbcContactFixture(id: UUID): Map<String, Any> {
        val data = HashMap<String, Any>()
        data["id"] = id
        data["phone_number"] = "+443700100222"
        data["fax"] = ""
        data["email"] = "yourquestions@bbc.co.uk"
        return data
    }


    /******************************* ORDERS *************************************/
    fun orderWithEmptyMerchantId(): String {
        return "    {\n" +
                "        \"buyer_email\" : \"sankha@xzy.com\",\n" +
                "        \"order_amount\" : \"1200.30\",\n" +
                "        \"currency_code\" : \"EUR\"\n" +
                "    }"
    }

    fun orderWitNoBuyer(): String {
        return "    {\n" +
                "        \"merchant_id\": \"7e8186aa-360a-46c2-aa38-d9ec60380eff\",\n" +
                "        \"order_amount\" : \"1200.30\",\n" +
                "        \"currency_code\" : \"EUR\"\n" +
                "    }"
    }

    fun orderPayload(orgId: String): String {
        return "    {\n" +
                "        \"buyer_email\" : \"sankha@xzy.com\",\n" +
                "        \"merchant_id\": \"" + orgId + "\",\n" +
                "        \"order_amount\" : \"1200.30\",\n" +
                "        \"currency_code\" : \"EUR\"\n" +
                "    }"
    }


    /****************************** INVOICES ***************************************/
    fun invoiceWithOutOrderId(): String {
        return "    {\n" +
                "        \"invoice_amount\" : \"12.30\",\n" +
                "        \"currency_code\" : \"EUR\"\n" +
                "    }"
    }

    fun invoicePayload(orderId: String): String {
        return "{\n" +
                "    \"order_id\" : \"" + orderId + "\",\n" +
                "    \"invoice_amount\" : \"12.30\",\n" +
                "    \"currency_code\" : \"EUR\"\n" +
                "}"
    }

    fun invoicePayloadWithAmount(orderId: String, amount:String, currencyCode : String): String {
        return "{\n" +
                "    \"order_id\" : \"" + orderId + "\",\n" +
                "    \"invoice_amount\" : \"" + amount + "\",\n" +
                "        \"currency_code\" : \"" + currencyCode + "\"\n" +
                "}"
    }

    /*************************** PAYMENTS ******************************************/
    fun paymentPayloadWoInvoiceId(): String {
        return "    {\n" +
                "        \"payment_amount\" : \"12.30\",\n" +
                "        \"currency_code\" : \"EUR\"\n" +
                "    }"
    }

    fun paymentPayload(invoiceId: String, amount:String, currencyCode : String): String {
        return "    {\n" +
                "        \"invoice_id\" : \"" + invoiceId + "\",\n" +
                "        \"payment_amount\" : \"" + amount + "\",\n" +
                "        \"currency_code\" : \"" + currencyCode + "\"\n" +
                "    }"
    }


    fun invoiceStatusFixture(statusType: InvoiceStatusType): Map<String, Any> {
        val data = HashMap<String, Any>()
        data["invoice_status"] = statusType.toString()
        return data
    }
}
