package com.davnig.invoicemanagementapi.model.dto.summary

import com.davnig.invoicemanagementapi.model.entity.TaxableSubject

class TaxableSubjectSummary(
    var piva: String? = null,
    var taxCode: String? = null
) {

    constructor(entity: TaxableSubject) : this(
        piva = entity.piva,
        taxCode = entity.taxCode
    )

}