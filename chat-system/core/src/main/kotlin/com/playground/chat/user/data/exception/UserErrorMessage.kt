package com.playground.chat.user.data.exception

import com.playground.chat.global.data.ErrorMessage

enum class UserErrorMessage(
    override val statusCode: Int,
    override val errorCode: String,
    override val message: String
): ErrorMessage {
    NOT_FOUND_USER(404, "NOT_FOUND_USER", "유저가 존재하지 않습니다."),
    DUPLICATED_EMAIL(400, "DUPLICATED_EMAIL", "중복된 이메일입니다.")
}
