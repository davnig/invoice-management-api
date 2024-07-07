package com.davnig.invoicemanagementapi.model.entity

import jakarta.persistence.*

@Entity
@Table(name = "t_flat_rate_scheme")
class FlatRateScheme {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flat-rate-scheme-id-gen")
    @SequenceGenerator(name = "flat-rate-scheme-id-gen", sequenceName = "t_flat_rate_scheme_id_seq", allocationSize = 1)
    var id = 0
    var name: String? = null
    var rate = 0f

}