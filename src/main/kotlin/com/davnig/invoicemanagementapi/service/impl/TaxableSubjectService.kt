package com.davnig.invoicemanagementapi.service.impl

import com.davnig.invoicemanagementapi.model.dto.Paginating
import com.davnig.invoicemanagementapi.model.dto.detail.TaxableSubjectDetail
import com.davnig.invoicemanagementapi.model.dto.summary.TaxableSubjectSummary
import com.davnig.invoicemanagementapi.model.entity.QTaxableSubject
import com.davnig.invoicemanagementapi.model.entity.TaxableSubject
import com.davnig.invoicemanagementapi.repository.TaxableSubjectRepository
import com.davnig.invoicemanagementapi.service.TaxableSubjectEntityService
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
class TaxableSubjectService(
    private val taxableSubjectRepository: TaxableSubjectRepository,
    entityManager: EntityManager
) : TaxableSubjectEntityService {

    @Autowired
    private lateinit var jpaQueryFactory: JPAQueryFactory
    private val qEntity: QTaxableSubject = QTaxableSubject.taxableSubject
    private val querydsl: Querydsl

    init {
        val entityInfo: JpaEntityInformation<TaxableSubject, *> =
            JpaEntityInformationSupport.getEntityInformation(TaxableSubject::class.java, entityManager)
        val entityPath = SimpleEntityPathResolver.INSTANCE.createPath(entityInfo.javaType)
        val entityPathBuilder: PathBuilder<TaxableSubject> = PathBuilder(entityPath.type, entityPath.metadata)
        querydsl = Querydsl(entityManager, entityPathBuilder)
    }

    override fun findAll(paginating: Paginating, search: String, fields: List<String>): Page<TaxableSubjectSummary> {
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
        val entities = query.fetch().map { TaxableSubjectSummary(it) }
        return PageableExecutionUtils.getPage(entities, pageable) { query.fetchCount() }
    }

    override fun findAll(paginating: Paginating, search: String): Page<TaxableSubjectSummary> {
        val pageable = paginating.getPageable()
        val decodedSearchMap = getSearchMapFromQueryStr(search)
        val predicate = buildEqPredicateFromSearchMap(decodedSearchMap)
        val query = querydsl.applyPagination(
            pageable, jpaQueryFactory
                .select(Projections.constructor(TaxableSubjectSummary::class.java, qEntity))
                .from(qEntity)
                .where(predicate)
        )
        val entities = query.fetch()
        return PageableExecutionUtils.getPage(entities, pageable) { query.fetchCount() }
    }

    override fun findAll(paginating: Paginating, fields: List<String>): Page<TaxableSubjectSummary> {
        val pageable = paginating.getPageable()
        val entityProjection = createEntityProjectionFrom(fields)
        val query = querydsl.applyPagination(
            pageable, jpaQueryFactory
                .select(entityProjection)
                .from(qEntity)
        )
        val entities = query.fetch().map { TaxableSubjectSummary(it) }
        return PageableExecutionUtils.getPage(entities, pageable) { query.fetchCount() }
    }

    override fun findAll(paginating: Paginating): Page<TaxableSubjectSummary> {
        val pageable = paginating.getPageable()
        val query = querydsl.applyPagination(
            pageable, jpaQueryFactory
                .select(Projections.constructor(TaxableSubjectSummary::class.java, qEntity))
                .from(qEntity)
        )
        val entities = query.fetch()
        return PageableExecutionUtils.getPage(entities, pageable) { query.fetchCount() }
    }

    override fun findById(id: Int): TaxableSubjectDetail {
        val entity = taxableSubjectRepository.findByIdOrNull(id) ?: throw EntityNotFoundException()
        return TaxableSubjectDetail(entity)
    }

    override fun findById(id: Int, fields: List<String>): TaxableSubjectDetail {
        val entityProjection = createEntityProjectionFrom(fields)
        val query = jpaQueryFactory
            .select(entityProjection)
            .from(qEntity)
            .where(qEntity.id.eq(id))
        val entity = query.fetchOne() ?: throw EntityNotFoundException()
        return TaxableSubjectDetail(entity)
    }

    override fun findByInvoiceId(invoiceId: Int): TaxableSubjectDetail {
        val entity = taxableSubjectRepository.findByInvoiceIdOrNull(invoiceId) ?: throw EntityNotFoundException()
        return TaxableSubjectDetail(entity)
    }

    override fun embedSubResources(
        id: Int,
        embed: List<String>,
        entityResource: TaxableSubjectDetail
    ): TaxableSubjectDetail {
        TODO("Not yet implemented")
    }

    private fun buildEqPredicateFromSearchMap(searchMap: Map<String, String>): BooleanBuilder {
        val predicate = BooleanBuilder()
        for ((searchKey, searchValue) in searchMap.entries) {
            val qClass = QTaxableSubject::class.java
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
    private fun createEntityProjectionFrom(fields: List<String>): QBean<TaxableSubject> {
        val qDeclaredFields: Array<Field> = QTaxableSubject::class.java.declaredFields
        val qFieldPaths = mutableListOf<Path<*>>()
        for (field in fields) {
            if (qDeclaredFields.any { qField -> qField.name == field }) {
                val qField = QTaxableSubject::class.java.getDeclaredField(field)
                qFieldPaths.add(qField.get(qEntity) as Path<*>)
            }
        }
        return Projections.bean(TaxableSubject::class.java, *qFieldPaths.toTypedArray())
    }

}