package com.playground.chat.chat.data.response

import java.util.*

data class ChatUserDto(
    val id: UUID,
    val image: String? = null,
    val nickname: String,
    val isOwner: Boolean,
    val isMe: Boolean
)
