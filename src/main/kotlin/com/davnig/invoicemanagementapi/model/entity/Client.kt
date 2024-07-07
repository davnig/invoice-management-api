package com.davnig.invoicemanagementapi.model.entity

import jakarta.persistence.*

@Entity
@Table(name = "t_client")
class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client-id-gen")
    @SequenceGenerator(name = "client-id-gen", sequenceName = "t_client_id_seq", allocationSize = 1)
    var id = 0
    var taxCode: String? = null
    var name: String? = null
    var address: String? = null

    @OneToMany
    var invoices: Set<Invoice> = mutableSetOf()

}