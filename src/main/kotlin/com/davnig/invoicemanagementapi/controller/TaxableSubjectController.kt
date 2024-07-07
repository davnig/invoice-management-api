package com.davnig.invoicemanagementapi.controller

import com.davnig.invoicemanagementapi.model.dto.Paginating
import com.davnig.invoicemanagementapi.model.dto.detail.TaxableSubjectDetail
import com.davnig.invoicemanagementapi.model.dto.summary.TaxableSubjectSummary
import com.davnig.invoicemanagementapi.service.TaxableSubjectEntityService
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/taxable-subjects")
class TaxableSubjectController(
    private val taxableSubjectService: TaxableSubjectEntityService
) {

    @GetMapping
    fun findAll(
        @ModelAttribute paginating: Paginating,
        @RequestParam(required = false) search: String?,
        @RequestParam(required = false) fields: List<String>?
    ): ResponseEntity<Page<TaxableSubjectSummary>> {
        return when {
            (search != null && fields != null) ->
                ResponseEntity.ok(taxableSubjectService.findAll(paginating, search, fields))

            (search != null) -> ResponseEntity.ok(taxableSubjectService.findAll(paginating, search))
            (fields != null) -> ResponseEntity.ok(taxableSubjectService.findAll(paginating, fields))
            else -> ResponseEntity.ok(taxableSubjectService.findAll(paginating))
        }
    }

    @GetMapping("/{id}")
    fun findById(
        @PathVariable id: Int,
        @RequestParam(required = false) fields: List<String>?
    ): ResponseEntity<TaxableSubjectDetail> {
        return when {
            (fields == null) -> ResponseEntity.ok(taxableSubjectService.findById(id))
            else -> ResponseEntity.ok(taxableSubjectService.findById(id, fields))
        }
    }

}