package com.playground.chat.global.data

import kotlin.math.ceil

data class Page<T>(
    val pageResult: PageResult,
    val content: T
) {
    data class PageResult(
        val page: Int,
        val size: Int,
        val totalPages: Int,
        val totalElements: Long,
        val isFirst: Boolean,
        val isLast: Boolean
    )

    companion object {
        fun <T>of(totalCount: Long, page: Int, size: Int, content: T): Page<T> {
            return Page(
                pageResult = PageResult(
                    page = page,
                    size = size,
                    totalPages = ceil(totalCount.toDouble() / size).toInt(),
                    totalElements = totalCount,
                    isFirst = page == 1,
                    isLast = (totalCount - ((page - 1) * size)) <= size,
                ),
                content = content
            )
        }
    }
}
