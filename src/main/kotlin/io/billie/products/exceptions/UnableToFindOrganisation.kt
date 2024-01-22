package io.billie.products.exceptions

import java.util.*

class UnableToFindOrganisation(merchantId: UUID) : RuntimeException("Unable to find orgranisation with $merchantId")
