package com.davnig.invoicemanagementapi.model.entity

import jakarta.persistence.*

@Entity
@Table(name = "t_profitability_coefficient")
class ProfitabilityCoefficient {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profitability-coeff-id-gen")
    @SequenceGenerator(
        name = "profitability-coeff-id-gen",
        sequenceName = "t_profitability_coefficient_id_seq",
        allocationSize = 1
    )
    var id = 0
    var coefficient = 0f
    var description: String? = null

}