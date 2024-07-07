package com.davnig.invoicemanagementapi.service.impl

import com.davnig.invoicemanagementapi.model.TaxProfileDefault
import com.davnig.invoicemanagementapi.model.dto.Paginating
import com.davnig.invoicemanagementapi.model.entity.QTaxProfile
import com.davnig.invoicemanagementapi.model.entity.TaxProfile
import com.davnig.invoicemanagementapi.repository.TaxProfileRepository
import com.davnig.invoicemanagementapi.service.EntityService
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Path
import com.querydsl.core.types.Projections
import com.querydsl.core.types.QBean
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.core.types.dsl.StringPath
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.EntityNotFoundException
import lombok.SneakyThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport
import org.springframework.data.jpa.repository.support.Querydsl
import org.springframework.data.querydsl.SimpleEntityPathResolver
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Service
import java.lang.reflect.Field

@Service
class TaxProfileService(
    private val taxProfileRepository: TaxProfileRepository,
    entityManager: EntityManager
) : EntityService<TaxProfileDefault, TaxProfileDefault> {

    @Autowired
    private lateinit var jpaQueryFactory: JPAQueryFactory
    private val qEntity: QTaxProfile = QTaxProfile.taxProfile
    private val querydsl: Querydsl

    init {
        val entityInfo: JpaEntityInformation<TaxProfile, *> =
            JpaEntityInformationSupport.getEntityInformation(TaxProfile::class.java, entityManager)
        val entityPath = SimpleEntityPathResolver.INSTANCE.createPath(entityInfo.javaType)
        val entityPathBuilder: PathBuilder<TaxProfile> = PathBuilder(entityPath.type, entityPath.metadata)
        querydsl = Querydsl(entityManager, entityPathBuilder)
    }

    override fun findAll(paginating: Paginating, search: String, fields: List<String>): Page<TaxProfileDefault> {
        val pageable = paginating.getPageable()
        val entityProjection = createEntityProjectionFrom(fields)
        val searchMap = getSearchMapFromQueryStr(search)
        val predicate = buildEqPredicateFromSearchMap(searchMap)
        val query = querydsl.applyPagination(
            pageable, jpaQueryFactory
                .select(entityProjection)
                .from(qEntity)
                .where(predicate)
        )
        val entities = query.fetch().map { TaxProfileDefault(it) }
        return PageableExecutionUtils.getPage(entities, pageable) { query.fetchCount() }
    }

    override fun findAll(paginating: Paginating, search: String): Page<TaxProfileDefault> {
        val pageable = paginating.getPageable()
        val decodedSearchMap = getSearchMapFromQueryStr(search)
        val predicate = buildEqPredicateFromSearchMap(decodedSearchMap)
        val query = querydsl.applyPagination(
            pageable, jpaQueryFactory
                .select(Projections.constructor(TaxProfileDefault::class.java, qEntity))
                .from(qEntity)
                .where(predicate)
        )
        val entities = query.fetch()
        return PageableExecutionUtils.getPage(entities, pageable) { query.fetchCount() }
    }

    override fun findAll(paginating: Paginating, fields: List<String>): Page<TaxProfileDefault> {
        val pageable = paginating.getPageable()
        val entityProjection = createEntityProjectionFrom(fields)
        val query = querydsl.applyPagination(
            pageable, jpaQueryFactory
                .select(entityProjection)
                .from(qEntity)
        )
        val entities = query.fetch().map { TaxProfileDefault(it) }
        return PageableExecutionUtils.getPage(entities, pageable) { query.fetchCount() }
    }

    override fun findAll(paginating: Paginating): Page<TaxProfileDefault> {
        val pageable = paginating.getPageable()
        val query = querydsl.applyPagination(
            pageable, jpaQueryFactory
                .select(Projections.constructor(TaxProfileDefault::class.java, qEntity))
                .from(qEntity)
        )
        val entities = query.fetch()
        return PageableExecutionUtils.getPage(entities, pageable) { query.fetchCount() }
    }

    override fun findById(id: Int): TaxProfileDefault {
        val entity = taxProfileRepository.findByIdOrNull(id) ?: throw EntityNotFoundException()
        return TaxProfileDefault(entity)
    }

    override fun findById(id: Int, fields: List<String>): TaxProfileDefault {
        val entityProjection = createEntityProjectionFrom(fields)
        val query = jpaQueryFactory
            .select(entityProjection)
            .from(qEntity)
            .where(qEntity.id.eq(id))
        val entity = query.fetchOne() ?: throw EntityNotFoundException()
        return TaxProfileDefault(entity)
    }

    override fun embedSubResources(id: Int, embed: List<String>, entityResource: TaxProfileDefault): TaxProfileDefault {
        TODO("Not yet implemented")
    }

    private fun buildEqPredicateFromSearchMap(searchMap: Map<String, String>): BooleanBuilder {
        val predicate = BooleanBuilder()
        for ((searchKey, searchValue) in searchMap.entries) {
            val qClass = QTaxProfile::class.java
            if (qClass.declaredFields.any { it.name.equals(searchKey) }) {
                when (val qFieldPath = qClass.getDeclaredField(searchKey).get(qEntity)) {
                    is StringPath -> predicate.and(qFieldPath.eq(searchValue))
                    is NumberPath<*> -> predicate.and(qFieldPath.stringValue().eq(searchValue))
                }
            }
        }
        return predicate
    }

    private fun getSearchMapFromQueryStr(search: String): MutableMap<String, String> {
        val searchMap = mutableMapOf<String, String>()
        val regex = Regex("(\\w+):(\\w+)")
        for (matchResult in regex.findAll(search)) {
            val (param, value) = matchResult.destructured
            searchMap[param] = value
        }
        return searchMap
    }

    @SneakyThrows
    private fun createEntityProjectionFrom(fields: List<String>): QBean<TaxProfile> {
        val qDeclaredFields: Array<Field> = QTaxProfile::class.java.declaredFields
        val qFieldPaths = mutableListOf<Path<*>>()
        for (field in fields) {
            if (qDeclaredFields.any { it.name == field }) {
                val qField = QTaxProfile::class.java.getDeclaredField(field)
                qFieldPaths.add(qField.get(qEntity) as Path<*>)
            }
        }
        return Projections.bean(TaxProfile::class.java, *qFieldPaths.toTypedArray())
    }

}