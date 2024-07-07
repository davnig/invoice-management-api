package com.davnig.invoicemanagementapi.repository

import com.davnig.invoicemanagementapi.model.entity.TaxableSubject
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository

@Repository
interface TaxableSubjectRepository : JpaRepository<TaxableSubject, Int>,
    QuerydslPredicateExecutor<TaxableSubject> {

    @Query(
        value = "select t.* from t_taxable_subject t inner join t_invoice i on t.id = i.id_vendor " +
                "where i.id = :invoiceId", nativeQuery = true
    )
    fun findByInvoiceIdOrNull(invoiceId: Int): TaxableSubject?

}