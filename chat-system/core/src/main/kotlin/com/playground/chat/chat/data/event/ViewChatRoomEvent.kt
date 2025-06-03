package com.playground.chat.chat.data.event

import java.time.Instant
import java.util.UUID

data class ViewChatRoomEvent(
    val userId: UUID,
    val roomId: UUID,
    val messageId: UUID?,
    val timestamp: Instant? = Instant.now()
)
