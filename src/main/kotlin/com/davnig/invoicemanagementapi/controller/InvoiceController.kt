package com.davnig.invoicemanagementapi.controller

import com.davnig.invoicemanagementapi.model.dto.Paginating
import com.davnig.invoicemanagementapi.model.dto.detail.InvoiceDetail
import com.davnig.invoicemanagementapi.model.dto.summary.InvoiceSummary
import com.davnig.invoicemanagementapi.service.EntityService
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/invoices")
class InvoiceController(
    private val invoiceService: EntityService<InvoiceSummary, InvoiceDetail>
) {

    @GetMapping
    fun findAll(
        @ModelAttribute paginating: Paginating,
        @RequestParam(required = false) search: String?,
        @RequestParam(required = false) fields: List<String>?
    ): ResponseEntity<Page<InvoiceSummary>> {
        return when {
            (search != null && fields != null) ->
                ResponseEntity.ok(invoiceService.findAll(paginating, search, fields))

            (search != null) -> ResponseEntity.ok(invoiceService.findAll(paginating, search))
            (fields != null) -> ResponseEntity.ok(invoiceService.findAll(paginating, fields))
            else -> ResponseEntity.ok(invoiceService.findAll(paginating))
        }
    }

    @GetMapping("/{id}")
    fun findById(
        @PathVariable id: Int,
        @RequestParam(required = false) fields: List<String>?,
        @RequestParam(required = false) embed: List<String>?
    ): ResponseEntity<InvoiceDetail> {
        return when {
            (fields != null && embed != null) -> {
                val dto = invoiceService.findById(id, fields)
                return ResponseEntity.ok(invoiceService.embedSubResources(id, embed, dto))
            }

            (fields != null) -> ResponseEntity.ok(invoiceService.findById(id, fields))
            (embed != null) -> {
                val dto = invoiceService.findById(id)
                return ResponseEntity.ok(invoiceService.embedSubResources(id, embed, dto))
            }

            else -> ResponseEntity.ok(invoiceService.findById(id))
        }
    }

}