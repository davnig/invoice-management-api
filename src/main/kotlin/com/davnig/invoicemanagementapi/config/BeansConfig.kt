package com.davnig.invoicemanagementapi.config

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BeansConfig {
    @PersistenceContext
    private val entityManager: EntityManager? = null

    @get:Bean
    val jPAQueryFactory: JPAQueryFactory
        get() = JPAQueryFactory(entityManager)
}