package com.playground.board.global.dto

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

data class ResponseDto<T>(
    val serverDataTime: LocalDateTime,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val pageResult: PageResult<T>?,
    val data: T
) {
    constructor(data: T): this(LocalDateTime.now(), null, data)
    constructor(pageResult: PageResult<T>, data: T): this(LocalDateTime.now(), pageResult, data)

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
        val isLast: Boolean? = null
    )
}
