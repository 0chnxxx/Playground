package com.playground.chat.chat.data.event

import java.time.Instant
import java.util.UUID

data class SendChatMessageEvent(
    val roomId: UUID,
    val userId: UUID,
    val nickname: String,
    val messageId: UUID?,
    val content: String,
    val timestamp: Instant = Instant.now()
)
