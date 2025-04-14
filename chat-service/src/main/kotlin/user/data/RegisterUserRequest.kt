package com.playground.chat.user.dto

data class RegisterUserRequest(
    val email: String,
    val password: String,
    val nickname: String
)
