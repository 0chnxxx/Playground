package com.playground.chat.channel.service

import com.playground.chat.chat.data.response.ChatMessageDto
import org.springframework.stereotype.Service

@Service
class ChannelService(
    private val chatMessagePublisher: ChatMessagePublisher
) {
    fun sendChatMessage(roomId: Long, message: ChatMessageDto) {
        chatMessagePublisher.publish(roomId, message)
    }
}
