package com.playground.chat.chat.data.response

import java.time.Instant
import java.util.UUID

data class ChatMessageDto(
    val messageId: UUID,
    val userId: UUID,
    val nickname: String,
    val content: String,
    val unreadUserIds: List<UUID> = emptyList(),
    val isMine: Boolean,
    val timestamp: Instant? = Instant.now()
) {
    constructor(
        messageId: UUID,
        userId: UUID,
        nickname: String,
        content: String,
        isMine: Boolean,
        timestamp: Instant? = Instant.now()
    ) : this(
        messageId = messageId,
        userId = userId,
        nickname = nickname,
        content = content,
        unreadUserIds = emptyList(),
        isMine = isMine,
        timestamp = timestamp
    )
}
