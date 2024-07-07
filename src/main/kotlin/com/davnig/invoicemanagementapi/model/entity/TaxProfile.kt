package com.davnig.invoicemanagementapi.model.entity

import jakarta.persistence.*

@Entity
@Table(name = "t_tax_profile")
class TaxProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tax_profile-id-gen")
    @SequenceGenerator(name = "tax_profile-id-gen", sequenceName = "t_tax_profile_id_seq", allocationSize = 1)
    var id = 0

    @ManyToOne
    @JoinColumn(name = "id_prof_coefficient", referencedColumnName = "id")
    var profitabilityCoefficient: ProfitabilityCoefficient? = null

    @ManyToOne
    @JoinColumn(name = "id_social_security_fund", referencedColumnName = "id")
    var socialSecurityFund: SocialSecurityFund? = null

    @ManyToOne
    @JoinColumn(name = "id_flat_rate_scheme", referencedColumnName = "id")
    var flatRateScheme: FlatRateScheme? = null

}