package com.playground.chat.chat.data.response

import java.time.Instant
import java.util.UUID

data class ChatMessageDto(
    val messageId: UUID,
    val userId: UUID?,
    val image: String? = null,
    val nickname: String,
    val type: String,
    val content: String,
    val unreadUserIds: List<UUID> = emptyList(),
    val isMine: Boolean,
    val timestamp: Instant? = Instant.now()
) {
    constructor(
        messageId: UUID,
        userId: UUID?,
        image: String?,
        nickname: String?,
        type: String,
        content: String,
        isMine: Boolean,
        timestamp: Instant? = Instant.now()
    ) : this(
        messageId = messageId,
        userId = userId,
        image = image,
        nickname = nickname ?: "SYSTEM",
        type = type,
        content = content,
        unreadUserIds = emptyList(),
        isMine = isMine,
        timestamp = timestamp
    )
}
