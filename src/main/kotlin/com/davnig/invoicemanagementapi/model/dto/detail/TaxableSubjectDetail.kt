package com.davnig.invoicemanagementapi.model.dto.detail

import com.davnig.invoicemanagementapi.model.entity.TaxableSubject

class TaxableSubjectDetail(
    var piva: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var taxCode: String? = null
) {

    constructor(entity: TaxableSubject) : this(
        piva = entity.piva,
        firstName = entity.firstName,
        lastName = entity.lastName,
        taxCode = entity.taxCode
    )

}