package com.playground.chat.chat.data.response

import java.time.LocalDateTime

data class MyChatRoomDto(
    val id: Long,
    val name: String,
    val lastMessage: String? = null,
    val unreadCount: Long,
    val lastSentAt: LocalDateTime? = null,
    val memberCount: Long
)
