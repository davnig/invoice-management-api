package com.davnig.invoicemanagementapi.service.impl

import com.davnig.invoicemanagementapi.model.dto.ClientDefault
import com.davnig.invoicemanagementapi.model.dto.Paginating
import com.davnig.invoicemanagementapi.model.entity.Client
import com.davnig.invoicemanagementapi.model.entity.Invoice
import com.davnig.invoicemanagementapi.model.entity.QClient
import com.davnig.invoicemanagementapi.repository.ClientRepository
import com.davnig.invoicemanagementapi.service.ClientEntityService
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
class ClientService(
    private val clientRepository: ClientRepository,
    entityManager: EntityManager
) : ClientEntityService {

    @Autowired
    private lateinit var jpaQueryFactory: JPAQueryFactory
    private val qEntity: QClient = QClient.client
    private val querydsl: Querydsl

    init {
        val entityInfo: JpaEntityInformation<Invoice, *> =
            JpaEntityInformationSupport.getEntityInformation(Invoice::class.java, entityManager)
        val entityPath = SimpleEntityPathResolver.INSTANCE.createPath(entityInfo.javaType)
        val entityPathBuilder: PathBuilder<Invoice> = PathBuilder(entityPath.type, entityPath.metadata)
        querydsl = Querydsl(entityManager, entityPathBuilder)
    }

    override fun findAll(paginating: Paginating, search: String, fields: List<String>): Page<ClientDefault> {
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
        val entities = query.fetch().map { ClientDefault(it) }
        return PageableExecutionUtils.getPage(entities, pageable) { query.fetchCount() }
    }

    override fun findAll(paginating: Paginating, search: String): Page<ClientDefault> {
        val pageable = paginating.getPageable()
        val decodedSearchMap = getSearchMapFromQueryStr(search)
        val predicate = buildEqPredicateFromSearchMap(decodedSearchMap)
        val query = querydsl.applyPagination(
            pageable, jpaQueryFactory
                .select(Projections.constructor(ClientDefault::class.java, qEntity))
                .from(qEntity)
                .where(predicate)
        )
        val entities = query.fetch()
        return PageableExecutionUtils.getPage(entities, pageable) { query.fetchCount() }
    }

    override fun findAll(paginating: Paginating, fields: List<String>): Page<ClientDefault> {
        val pageable = paginating.getPageable()
        val entityProjection = createEntityProjectionFrom(fields)
        val query = querydsl.applyPagination(
            pageable, jpaQueryFactory
                .select(entityProjection)
                .from(qEntity)
        )
        val entities = query.fetch().map { ClientDefault(it) }
        return PageableExecutionUtils.getPage(entities, pageable) { query.fetchCount() }
    }

    override fun findAll(paginating: Paginating): Page<ClientDefault> {
        val pageable = paginating.getPageable()
        val query = querydsl.applyPagination(
            pageable, jpaQueryFactory
                .select(Projections.constructor(ClientDefault::class.java, qEntity))
                .from(qEntity)
        )
        val entities = query.fetch()
        return PageableExecutionUtils.getPage(entities, pageable) { query.fetchCount() }
    }

    override fun findById(id: Int): ClientDefault {
        val entity = clientRepository.findByIdOrNull(id) ?: throw EntityNotFoundException()
        return ClientDefault(entity)
    }

    override fun findById(id: Int, fields: List<String>): ClientDefault {
        val entityProjection = createEntityProjectionFrom(fields)
        val query = jpaQueryFactory
            .select(entityProjection)
            .from(qEntity)
            .where(qEntity.id.eq(id))
        val entity = query.fetchOne() ?: throw EntityNotFoundException()
        return ClientDefault(entity)
    }

    override fun findByInvoiceId(invoiceId: Int): ClientDefault {
        val entity = clientRepository.findByInvoiceIdOrNull(invoiceId) ?: throw EntityNotFoundException()
        return ClientDefault(entity)
    }

    override fun embedSubResources(id: Int, embed: List<String>, entityResource: ClientDefault): ClientDefault {
        TODO("Not yet implemented")
    }

    private fun buildEqPredicateFromSearchMap(searchMap: Map<String, String>): BooleanBuilder {
        val predicate = BooleanBuilder()
        for ((searchKey, searchValue) in searchMap.entries) {
            val qClass = QClient::class.java
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
    private fun createEntityProjectionFrom(fields: List<String>): QBean<Client> {
        val qDeclaredFields: Array<Field> = QClient::class.java.declaredFields
        val qFieldPaths = mutableListOf<Path<*>>()
        for (field in fields) {
            if (qDeclaredFields.any { it.name == field }) {
                val qField = QClient::class.java.getDeclaredField(field)
                qFieldPaths.add(qField.get(qEntity) as Path<*>)
            }
        }
        return Projections.bean(Client::class.java, *qFieldPaths.toTypedArray())
    }

}