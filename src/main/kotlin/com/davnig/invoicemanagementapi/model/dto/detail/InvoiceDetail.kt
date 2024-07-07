package com.davnig.invoicemanagementapi.model.dto.detail

import com.davnig.invoicemanagementapi.model.dto.ClientDefault
import com.davnig.invoicemanagementapi.model.entity.Invoice

class InvoiceDetail(
    var number: Int? = null,
    var date: String? = null,
    var idVendor: Int? = null,
    var idClient: Int? = null,
    var vendor: TaxableSubjectDetail? = null,
    var client: ClientDefault? = null
) {

    constructor(entity: Invoice) : this(
        number = entity.number,
        date = entity.date.toString(),
        idVendor = entity.idVendor,
        idClient = entity.idClient
    )

}