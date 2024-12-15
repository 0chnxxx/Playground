package com.playground.board.global.dto

import java.time.LocalDateTime

data class ErrorResponseDto(
    val serverDateTime: LocalDateTime = LocalDateTime.now(),
    val errorCode: String,
    val errorMessage: String,
    val path: String
) {
    constructor(errorCode: String, errorMessage: String, path: String): this(LocalDateTime.now(), errorCode, errorMessage, path)
}
