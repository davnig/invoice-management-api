package com.davnig.invoicemanagementapi.controller

import com.davnig.invoicemanagementapi.model.dto.ClientDefault
import com.davnig.invoicemanagementapi.service.ClientEntityService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ClientController(
    private val clientService: ClientEntityService
) {

    @GetMapping("/invoices/{id}/client")
    fun findByInvoiceId(@PathVariable(name = "id") invoiceId: Int): ResponseEntity<ClientDefault> {
        return ResponseEntity.ok(clientService.findByInvoiceId(invoiceId))
    }


}