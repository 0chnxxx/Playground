package com.playground.chat.chat.data.response

import java.time.Instant
import java.util.UUID

data class ChatRoomDto(
    val id: UUID,
    val image: String? = null,
    val name: String,
    val lastMessage: String? = null,
    val lastSentAt: Instant? = null,
    val memberCount: Long,
    val isJoined: Boolean
)
