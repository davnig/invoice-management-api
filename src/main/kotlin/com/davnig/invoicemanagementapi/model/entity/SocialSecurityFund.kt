package com.davnig.invoicemanagementapi.model.entity

import jakarta.persistence.*

@Entity
@Table(name = "t_social_security")
class SocialSecurityFund {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "social-security-id-gen")
    @SequenceGenerator(name = "social-security-id-gen", sequenceName = "t_social_security_id_seq", allocationSize = 1)
    var id = 0
    var name: String? = null
    var rate = 0f

}