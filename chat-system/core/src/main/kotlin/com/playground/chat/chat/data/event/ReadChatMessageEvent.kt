package com.playground.chat.chat.data.event

import java.time.LocalDateTime

data class ReadChatMessageEvent(
    val type: Type = Type.ONE,
    val roomId: Long,
    val userId: Long,
    val messageId: Long,
    val timestamp: LocalDateTime? = LocalDateTime.now()
) {
    enum class Type {
        ALL, ONE
    }
}
