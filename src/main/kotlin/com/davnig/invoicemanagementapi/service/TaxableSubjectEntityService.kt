package com.davnig.invoicemanagementapi.service

import com.davnig.invoicemanagementapi.model.dto.detail.TaxableSubjectDetail
import com.davnig.invoicemanagementapi.model.dto.summary.TaxableSubjectSummary

interface TaxableSubjectEntityService : EntityService<TaxableSubjectSummary, TaxableSubjectDetail> {

    fun findByInvoiceId(invoiceId: Int): TaxableSubjectDetail

}