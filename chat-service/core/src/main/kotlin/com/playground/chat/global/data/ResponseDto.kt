package com.playground.chat.global.data

data class ResponseDto<T>(
    val timestamp: Long,
    val pageResult: PageResult? = null,
    val data: T,
) {
    data class PageResult(
        val totalPages: Int,
        val totalElements: Long,
        val isFirst: Boolean,
        val isLast: Boolean
    )

    companion object {
        fun <T>of(data: T): ResponseDto<T> {
            return ResponseDto(
                timestamp = System.currentTimeMillis(),
                data = data
            )
        }

        fun <T>of(page: Page<T>): ResponseDto<T> {
            return ResponseDto(
                timestamp = System.currentTimeMillis(),
                pageResult = PageResult(
                    totalPages = page.totalPages,
                    totalElements = page.totalElements,
                    isFirst = page.isFirst,
                    isLast = page.isLast
                ),
                data = page.data
            )
        }
    }
}
