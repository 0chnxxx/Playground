package com.playground.chat.global.dto

data class ResponseDto<T>(
    val timestamp: Long,
    val data: T
) {
    companion object {
        fun <T>of(data: T): ResponseDto<T> {
            return ResponseDto(
                timestamp = System.currentTimeMillis(),
                data = data
            )
        }
    }
}
