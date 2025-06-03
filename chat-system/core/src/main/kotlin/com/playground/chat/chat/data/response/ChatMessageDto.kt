package com.playground.chat.chat.data.response

import java.time.LocalDateTime

data class ChatMessageDto(
    val messageId: Long,
    val userId: Long,
    val nickname: String,
    val content: String,
    val unreadUserIds: List<Long> = emptyList(),
    val isMine: Boolean,
    val timestamp: LocalDateTime? = LocalDateTime.now()
) {
    constructor(
        messageId: Long,
        userId: Long,
        nickname: String,
        content: String,
        isMine: Boolean,
        timestamp: LocalDateTime? = LocalDateTime.now()
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
