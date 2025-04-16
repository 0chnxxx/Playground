package com.playground.chat.user.data.request

data class RegisterUserRequest(
    val email: String,
    val password: String,
    val nickname: String
)
