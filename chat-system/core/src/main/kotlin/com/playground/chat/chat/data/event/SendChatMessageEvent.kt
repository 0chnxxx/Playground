package com.playground.chat.chat.data.event

import com.playground.chat.global.util.UuidUtil
import java.time.Instant
import java.util.UUID

data class SendChatMessageEvent(
    val roomId: UUID,
    val userId: UUID? = null,
    val nickname: String,
    val messageId: UUID = UuidUtil.generateUuidV7(),
    val type: String,
    val content: String,
    val timestamp: Instant = Instant.now()
)
