package io.billie.products.exceptions

import java.util.*

class UnableToFindOrderException(order: UUID) : RuntimeException("Order Id: $order can't be found")
