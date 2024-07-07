package com.davnig.invoicemanagementapi.model.entity

import jakarta.persistence.*

@Entity
@Table(name = "t_invoice_item")
class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice-item-id-gen")
    @SequenceGenerator(name = "invoice-item-id-gen", sequenceName = "t_invoice_item_id_seq", allocationSize = 1)
    var id = 0

    @ManyToOne
    var type: InvoiceItemType? = null
    var description: String? = null
    var price = 0
    var quantity = 0

}