package io.billie.products.exceptions

class UnableToFindCountry(val countryCode: String) : RuntimeException("Country Code not found $countryCode")
