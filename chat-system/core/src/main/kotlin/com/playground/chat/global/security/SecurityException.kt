package com.playground.chat.global.security

import com.playground.chat.global.data.CustomException
import com.playground.chat.global.data.ErrorMessage

class SecurityException(
    override val errorMessage: ErrorMessage
): CustomException(
    errorMessage
)
