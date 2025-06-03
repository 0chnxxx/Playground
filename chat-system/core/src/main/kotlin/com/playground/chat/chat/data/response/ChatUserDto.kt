package com.playground.chat.chat.data.response

import java.util.UUID

data class ChatUserDto(
    val id: UUID,
    val nickname: String,
    val isOwner: Boolean
)
