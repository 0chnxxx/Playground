package com.playground.board.core.dto

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

data class ResponseDto<T>(
    val status: Int,
    val statusName: String,
    val serverDataTime: LocalDateTime = LocalDateTime.now(),
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val pageResult: PageResult<T>? = null,
    val data: T
) {
    data class PageResult<P>(
        @JsonInclude(JsonInclude.Include.NON_NULL)
        val totalElements: Long? = null,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        val totalPages: Int? = null,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        val page: Int? = null,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        val size: Int? = null,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        val isFirst: Boolean? = null,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        val isLast: Boolean? = null,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        val prev: P? = null,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        val next: P? = null
    )
}
