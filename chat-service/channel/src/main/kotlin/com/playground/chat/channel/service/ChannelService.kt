package com.playground.chat.channel.service

import com.playground.chat.chat.domain.ChatMessage
import org.springframework.stereotype.Service

@Service
class ChannelService(
    private val chatMessagePublisher: ChatMessagePublisher
) {
    fun sendChatMessage(roomId: String, message: ChatMessage) {
        chatMessagePublisher.publish(roomId, message)
    }
}
