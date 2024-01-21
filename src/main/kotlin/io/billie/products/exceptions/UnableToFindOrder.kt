package io.billie.products.exceptions

import java.util.*

class UnableToFindOrder(val order: UUID) : RuntimeException()
