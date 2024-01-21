package io.billie.products.exceptions

import java.util.*

class InvalidInvoiceAmount(val order: String) : RuntimeException()
