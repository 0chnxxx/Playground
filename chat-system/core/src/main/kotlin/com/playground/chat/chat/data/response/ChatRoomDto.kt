package com.playground.chat.chat.data.response

import java.time.LocalDateTime
import java.util.UUID

data class ChatRoomDto(
    val id: UUID,
    val name: String,
    val lastMessage: String? = null,
    val lastSentAt: LocalDateTime? = null,
    val memberCount: Long,
    val isJoined: Boolean
)
