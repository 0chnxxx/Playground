package com.playground.chat.global.security

import com.playground.chat.global.data.ErrorMessage

enum class SecurityErrorMessage(
    override val statusCode: Int,
    override val errorCode: String,
    override val message: String
): ErrorMessage {
    UNAUTHORIZED(401, "UNAUTHORIZED", "인증되지 않았습니다."),
    FORBIDDEN(403, "FORBIDDEN", "권한이 없습니다."),
}
