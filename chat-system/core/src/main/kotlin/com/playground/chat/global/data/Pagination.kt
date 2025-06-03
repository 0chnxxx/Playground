package com.playground.chat.global.data

import kotlin.math.ceil

data class Pagination<T>(
    val page: Int,
    val size: Int,
    val totalPages: Int,
    val totalElements: Long,
    val isFirst: Boolean,
    val isLast: Boolean,
    val data: T
) {
    companion object {
        fun <T>of(totalCount: Long, page: Int, size: Int, data: T): Pagination<T> {
            return Pagination(
                page = page,
                size = size,
                totalPages = ceil(totalCount.toDouble() / size).toInt(),
                totalElements = totalCount,
                isFirst = page == 1,
                isLast = (totalCount - ((page - 1) * size)) <= size,
                data = data
            )
        }
    }
}
