package com.playground.chat.user.data.request

import com.playground.chat.global.security.PrincipalRole
import com.playground.chat.user.domain.User

data class RegisterUserRequest(
    val email: String,
    val password: String,
    val nickname: String
) {
    fun toUser(): User {
        return User(
            email = email,
            password = password,
            nickname = nickname,
            role = PrincipalRole.USER
        )
    }
}
