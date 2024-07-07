package com.davnig.invoicemanagementapi.repository

import com.davnig.invoicemanagementapi.model.entity.Client
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository

@Repository
interface ClientRepository : JpaRepository<Client, Int>, QuerydslPredicateExecutor<Client> {

    @Query(
        value = "select c.* from t_client c inner join t_invoice i on c.id = i.id_client where i.id = :invoiceId",
        nativeQuery = true
    )
    fun findByInvoiceIdOrNull(invoiceId: Int): Client?

}