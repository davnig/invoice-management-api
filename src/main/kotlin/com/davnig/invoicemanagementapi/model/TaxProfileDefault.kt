package com.davnig.invoicemanagementapi.model

import com.davnig.invoicemanagementapi.model.entity.TaxProfile

class TaxProfileDefault(
    var idProfitabilityCoefficient: Int?,
    var idSocialSecurityFund: Int?,
    var idFlatRateScheme: Int?
) {

    constructor(entity: TaxProfile) : this(
        idProfitabilityCoefficient = entity.profitabilityCoefficient?.id,
        idSocialSecurityFund = entity.socialSecurityFund?.id,
        idFlatRateScheme = entity.flatRateScheme?.id
    )

}