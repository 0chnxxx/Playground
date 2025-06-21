package com.playground.chat.global.data

abstract class CustomException(
    open val errorMessage: ErrorMessage
): RuntimeException(errorMessage.message)

