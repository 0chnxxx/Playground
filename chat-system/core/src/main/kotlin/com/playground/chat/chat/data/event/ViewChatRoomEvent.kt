package com.playground.chat.chat.data.event

import java.time.LocalDateTime

data class ViewChatRoomEvent(
    val userId: Long,
    val roomId: Long,
    val messageId: Long,
    val timestamp: LocalDateTime? = LocalDateTime.now()
)
