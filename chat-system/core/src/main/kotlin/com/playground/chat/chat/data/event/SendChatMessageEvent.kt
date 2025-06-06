package com.playground.chat.chat.data.event

import com.playground.chat.global.util.UuidUtil
import java.time.Instant
import java.util.UUID

data class SendChatMessageEvent(
    val roomId: UUID,
    var userId: UUID? = null,
    var image: String? = null,
    var nickname: String? = null,
    val messageId: UUID = UuidUtil.generateUuidV7(),
    val type: String,
    val content: String,
    val timestamp: Instant = Instant.now()
) {
    fun setSender(userId: UUID?, image: String?, nickname: String) {
        this.userId = userId
        this.image = image
        this.nickname = nickname
    }
}
