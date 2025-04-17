package com.playground.chat.chat.data.response

import java.time.LocalDateTime

data class ChatRoomDto(
    val id: Long,
    val name: String,
    val lastMessage: String? = null,
    val lastMessageTime: LocalDateTime? = null,
    val participantCount: Long,
    val isJoined: Boolean
)
