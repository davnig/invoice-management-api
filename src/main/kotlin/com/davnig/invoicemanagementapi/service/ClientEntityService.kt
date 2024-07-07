package com.davnig.invoicemanagementapi.service

import com.davnig.invoicemanagementapi.model.dto.ClientDefault

interface ClientEntityService : EntityService<ClientDefault, ClientDefault> {

    fun findByInvoiceId(invoiceId: Int): ClientDefault

}