package com.playground.chat.chat.data.response

import java.time.LocalDateTime

data class ChatRoomDto(
    val id: Long,
    val name: String,
    val lastMessage: String? = null,
    val lastSentAt: LocalDateTime? = null,
    val memberCount: Long,
    val isJoined: Boolean
)
