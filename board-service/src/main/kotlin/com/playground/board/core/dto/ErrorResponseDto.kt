package com.playground.board.core.dto

import java.time.LocalDateTime

data class ErrorResponseDto(
    val status: Int,
    val statusName: String,
    val serverDateTime: LocalDateTime = LocalDateTime.now(),
    val errorCode: String,
    val errorMessage: String,
    val path: String
)
