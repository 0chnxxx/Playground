package com.playground.chat.chat.data.event

import java.time.Instant
import java.util.UUID

data class ReadChatMessageEvent(
    val type: Type = Type.ONE,
    val roomId: UUID,
    val userId: UUID,
    val messageId: UUID,
    val timestamp: Instant? = Instant.now()
) {
    enum class Type {
        ALL, ONE
    }
}
