package io.billie.products.exceptions

import java.util.*

class InvalidBuyerException() : RuntimeException("Buyer shouldn't be null or empty")
