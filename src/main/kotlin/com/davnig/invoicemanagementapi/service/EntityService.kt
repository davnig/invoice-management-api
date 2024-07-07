package com.davnig.invoicemanagementapi.service

import com.davnig.invoicemanagementapi.model.dto.Paginating
import org.springframework.data.domain.Page

interface EntityService<S, D> {

    fun findAll(paginating: Paginating, search: String, fields: List<String>): Page<S>

    fun findAll(paginating: Paginating, search: String): Page<S>

    fun findAll(paginating: Paginating, fields: List<String>): Page<S>

    fun findAll(paginating: Paginating): Page<S>

    fun findById(id: Int): D

    fun findById(id: Int, fields: List<String>): D

    fun embedSubResources(id: Int, embed: List<String>, entityResource: D): D

}