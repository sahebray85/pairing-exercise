package io.billie.products.exceptions

class InvalidInvoiceAmountException(order: String) : RuntimeException(order)
