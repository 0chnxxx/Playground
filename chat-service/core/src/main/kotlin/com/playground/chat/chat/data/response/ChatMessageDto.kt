package com.playground.chat.chat.data.response

import java.time.LocalDateTime

data class ChatMessageDto(
    val messageId: Long,
    val userId: Long,
    val nickname: String,
    val content: String,
    val unreadCount: Long,
    val isMine: Boolean,
    val timestamp: LocalDateTime? = LocalDateTime.now()
)
