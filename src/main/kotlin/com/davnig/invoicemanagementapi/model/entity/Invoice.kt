package com.davnig.invoicemanagementapi.model.entity

import jakarta.persistence.*
import java.sql.Timestamp

@Entity
@Table(name = "t_invoice")
class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice-id-gen")
    @SequenceGenerator(name = "invoice-id-gen", sequenceName = "t_invoice_id_seq", allocationSize = 1)
    var id = 0
    var number = 0

    @Column(name = "emission_date")
    var date: Timestamp? = null
    val idVendor = 0
    val idClient = 0

}