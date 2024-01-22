package io.billie.products.exceptions

import java.util.*

class UnableToFindOrganisationException(merchantId: UUID) : RuntimeException("Unable to find organisation with $merchantId")
