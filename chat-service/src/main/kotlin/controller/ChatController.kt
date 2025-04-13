package com.playground.chat.controller

import com.playground.chat.dto.MessageDto
import com.playground.chat.service.ChatService
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatController(
    private val chatService: ChatService
) {
    @MessageMapping("/chat/rooms/{roomId}/send")
    fun sendMessage(messageDto: MessageDto) {
        chatService.sendMessage(messageDto)
    }
}
