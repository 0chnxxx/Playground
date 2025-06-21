package com.playground.chat.global.data

import java.time.Instant

sealed class Response<T> {
    data class Success<T>(
        val serverTime: Instant,
        val data: T,
    ): Response<T>()

    data class Fail(
        val serverTime: Instant,
        val error: Error
    ): Response<Error>()

    companion object {
        fun <T>success(data: T): Success<T> {
            return Success(
                serverTime = Instant.now(),
                data = data
            )
        }

        fun fail(error: Error): Fail {
            return Fail(
                serverTime = Instant.now(),
                error = error
            )
        }
    }
}
