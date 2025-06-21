package com.playground.chat.chat.data.exception

import com.playground.chat.global.data.CustomException
import com.playground.chat.global.data.ErrorMessage

class ChatException(
    override val errorMessage: ErrorMessage,
): CustomException(
    errorMessage
)
