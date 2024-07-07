package com.davnig.invoicemanagementapi.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "t_taxable_subject")
class TaxableSubject {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "taxable-subject-id-gen")
    @SequenceGenerator(name = "taxable-subject-id-gen", sequenceName = "t_taxable_subject_id_seq", allocationSize = 1)
    @JsonIgnore
    var id: Int = 0

    @Column(name = "p_iva")
    var piva: String? = null
    var firstName: String? = null
    var lastName: String? = null
    var taxCode: String? = null

}