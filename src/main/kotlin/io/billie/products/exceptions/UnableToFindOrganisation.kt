package io.billie.products.exceptions

import java.util.*

class UnableToFindOrganisation(val merchantId: UUID) : RuntimeException()
