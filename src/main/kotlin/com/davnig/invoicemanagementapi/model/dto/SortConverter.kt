package com.davnig.invoicemanagementapi.model.dto

import org.springframework.data.domain.Sort

class SortConverter {

    enum class SortDirection(val value: String) {

        ASCENDING("+") {
            override fun parse(): Sort.Direction {
                return Sort.Direction.ASC
            }
        },
        DESCENDING("-") {
            override fun parse(): Sort.Direction {
                return Sort.Direction.DESC
            }
        };

        abstract fun parse(): Sort.Direction

    }

    companion object {
        fun parse(sortOrder: String): Sort {
            return when (sortOrder[0].toString()) {
                SortDirection.ASCENDING.value -> Sort.by(Sort.Direction.ASC, sortOrder.substring(1))
                SortDirection.DESCENDING.value -> Sort.by(Sort.Direction.DESC, sortOrder.substring(1))
                else -> {
                    throw IllegalArgumentException()
                }
            }
        }
    }

}