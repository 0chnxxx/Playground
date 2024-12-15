package com.playground.board.global.exception

import com.playground.board.global.dto.ErrorResponseDto
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.resource.NoResourceFoundException
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(CustomException::class)
    fun handleCustomException(
        e: CustomException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponseDto> {
        val errorResponse = ErrorResponseDto(
            errorCode = e.errorCode.code,
            errorMessage = e.errorCode.message,
            path = request.requestURI
        )

        return ResponseEntity(errorResponse, HttpStatus.valueOf(e.errorCode.status))
    }

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFoundException(
        e: NoHandlerFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponseDto> {
        val errorCode = ErrorCode.NOT_FOUND_ENDPOINT

        val errorResponse = ErrorResponseDto(
            errorCode = errorCode.code,
            errorMessage = errorCode.message,
            path = request.requestURI
        )

        return ResponseEntity(errorResponse, HttpStatus.valueOf(errorCode.status))
    }

    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResourceFoundException(
        e: NoResourceFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponseDto> {
        val errorCode = ErrorCode.NOT_FOUND_RESOURCE

        val errorResponse = ErrorResponseDto(
            errorCode = errorCode.code,
            errorMessage = errorCode.message,
            path = request.requestURI
        )

        return ResponseEntity(errorResponse, HttpStatus.valueOf(errorCode.status))
    }

    @ExceptionHandler(Exception::class)
    fun handleException(
        e: Exception,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponseDto> {
        val errorCode = ErrorCode.INTERNAL_SERVER_ERROR

        val errorResponse = ErrorResponseDto(
            errorCode = errorCode.code,
            errorMessage = errorCode.message,
            path = request.requestURI
        )

        e.printStackTrace()

        return ResponseEntity(errorResponse, HttpStatus.valueOf(errorCode.status))
    }
}
