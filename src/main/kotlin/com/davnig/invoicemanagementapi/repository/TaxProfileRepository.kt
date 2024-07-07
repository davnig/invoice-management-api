package com.davnig.invoicemanagementapi.repository

import com.davnig.invoicemanagementapi.model.entity.TaxProfile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository

@Repository
interface TaxProfileRepository : JpaRepository<TaxProfile, Int>, QuerydslPredicateExecutor<TaxProfile> {
}