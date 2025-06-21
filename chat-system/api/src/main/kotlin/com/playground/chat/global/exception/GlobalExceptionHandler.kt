package com.playground.chat.global.exception

import com.playground.chat.global.data.CustomException
import com.playground.chat.global.data.Error
import com.playground.chat.global.data.Response
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(CustomException::class)
    fun handleCustomException(e: CustomException): ResponseEntity<Response<Error>> {
        val statusCode = e.errorMessage.statusCode
        val error = Error(
            errorCode = e.errorMessage.errorCode,
            message = e.errorMessage.message
        )
        val response = Response.fail(error)

        return ResponseEntity.status(statusCode).body(response)
    }

    @ExceptionHandler(Exception::class)
    fun handleCustomException(e: Exception): ResponseEntity<Response<Error>> {
        val statusCode = 500
        val error = Error(
            errorCode = "INTERNAL_SERVER_ERROR",
            message = e.message ?: "서버 에러입니다."
        )
        val response = Response.fail(error)

        return ResponseEntity.status(statusCode).body(response)
    }
}
