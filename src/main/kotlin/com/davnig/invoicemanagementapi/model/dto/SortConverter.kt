package com.davnig.invoicemanagementapi.model.dto

import org.springframework.data.domain.Sort

class SortConverter {

    enum class SortDirection(val value: String) {

        ASCENDING("asc") {
            override fun parse(): Sort.Direction {
                return Sort.Direction.ASC
            }
        },
        DESCENDING("desc") {
            override fun parse(): Sort.Direction {
                return Sort.Direction.DESC
            }
        };

        abstract fun parse(): Sort.Direction

    }

    companion object {
        fun parse(sortOrder: String, delimiter: String): Sort {
            val split = sortOrder.split(delimiter)
            val direction = split[0]
            val value = split[1]
            if (!sortOrder.contains(delimiter)) return Sort.by(Sort.Direction.ASC, direction)
            return when (direction) {
                SortDirection.ASCENDING.value -> Sort.by(Sort.Direction.ASC, value)
                SortDirection.DESCENDING.value -> Sort.by(Sort.Direction.DESC, value)
                else -> {
                    throw IllegalArgumentException()
                }
            }
        }
    }

}