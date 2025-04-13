package com.playground.chat.service

import com.playground.chat.dto.MessageDto
import org.springframework.stereotype.Service

@Service
class ChatService(
    private val chatPublisher: ChatPublisher
) {
    fun sendMessage(messageDto: MessageDto) {
        chatPublisher.publish(messageDto)
    }
}
