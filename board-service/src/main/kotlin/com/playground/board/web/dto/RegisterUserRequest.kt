package com.playground.board.web.dto

import com.playground.board.core.dto.`in`.RegisterUserCommand

data class RegisterUserRequest(
    val email: String,
    val password: String
) {
    fun toCommand(): RegisterUserCommand {
        return RegisterUserCommand(
            email = email,
            password = password
        )
    }
}
