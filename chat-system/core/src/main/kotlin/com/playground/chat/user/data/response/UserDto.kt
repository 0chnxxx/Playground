package com.playground.chat.user.data.response

import java.util.UUID

data class UserDto(
    val id: UUID,
    val image: String? = null,
    val nickname: String
)
