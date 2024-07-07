package com.davnig.invoicemanagementapi.model.dto

import com.davnig.invoicemanagementapi.model.entity.Client

class ClientDefault(
    var taxCode: String? = null,
    var name: String? = null,
    var address: String? = null
) {

    constructor(entity: Client) : this(
        taxCode = entity.taxCode,
        name = entity.name,
        address = entity.address
    )

}