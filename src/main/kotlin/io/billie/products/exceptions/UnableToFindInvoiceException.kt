package io.billie.products.exceptions

import java.util.*

class UnableToFindInvoiceException(invoice: UUID) : RuntimeException("Invoice Id: $invoice can't be found")
