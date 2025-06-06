package com.playground.chat.global.data

import java.time.Instant

data class Response<T>(
    val serverTime: Instant,
    val pageResult: PageResult? = null,
    val data: T,
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
        fun <T>of(data: T): Response<T> {
            return Response(
                serverTime = Instant.now(),
                data = data
            )
        }

        fun <T>of(pagination: Pagination<T>): Response<T> {
            return Response(
                serverTime = Instant.now(),
                pageResult = PageResult(
                    page = pagination.page,
                    size = pagination.size,
                    totalPages = pagination.totalPages,
                    totalElements = pagination.totalElements,
                    isFirst = pagination.isFirst,
                    isLast = pagination.isLast
                ),
                data = pagination.data
            )
        }
    }
}
