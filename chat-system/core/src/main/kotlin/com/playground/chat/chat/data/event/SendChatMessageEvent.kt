package com.playground.chat.chat.data.event

import java.time.Instant
import java.util.UUID

data class SendChatMessageEvent(
    val roomId: UUID,
    val userId: UUID? = null,
    val image: String? = null,
    val nickname: String? = null,
    val messageId: UUID? = null,
    val type: String,
    val content: String,
    val timestamp: Instant = Instant.now()
) {
    fun setMessageId(messageId: UUID): SendChatMessageEvent {
        return SendChatMessageEvent(
            roomId = this.roomId,
            userId = this.userId,
            image = this.image,
            nickname = this.nickname,
            messageId = messageId,
            type = this.type,
            content = this.content,
            timestamp = this.timestamp
        )
    }

    fun setSender(userId: UUID?, image: String?, nickname: String): SendChatMessageEvent {
        return SendChatMessageEvent(
            roomId = this.roomId,
            userId = userId,
            image = image,
            nickname = nickname,
            messageId = this.messageId,
            type = this.type,
            content = this.content,
            timestamp = this.timestamp
        )
    }
}
