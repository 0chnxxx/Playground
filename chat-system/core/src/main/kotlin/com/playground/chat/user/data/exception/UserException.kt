package com.playground.chat.user.data.exception

import com.playground.chat.global.data.CustomException
import com.playground.chat.global.data.ErrorMessage

class UserException(
    override val errorMessage: ErrorMessage
): CustomException(
    errorMessage
)
