package com.playground.chat.chat.data.event

import java.time.LocalDateTime

data class SendChatMessageEvent(
    val roomId: Long,
    val userId: Long,
    val nickname: String,
    val messageId: Long,
    val content: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
