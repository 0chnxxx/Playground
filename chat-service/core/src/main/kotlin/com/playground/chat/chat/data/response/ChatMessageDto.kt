package com.playground.chat.chat.data.response

import java.time.LocalDateTime

data class ChatMessageDto(
    val roomId: Long,
    val userId: Long,
    val nickname: String,
    val content: String,
    val isMine: Boolean,
    val timestamp: LocalDateTime? = LocalDateTime.now()
)
