package com.playground.chat.socket.controller

import com.playground.chat.chat.domain.ChatMessage
import com.playground.chat.socket.service.SocketPublisher
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SocketController(
    private val socketPublisher: SocketPublisher
) {
    /**
     * 채팅 메시지 전송
     */
    @MessageMapping("/chat/rooms/{roomId}/messages/send")
    fun sendChatMessage(
        chatMessage: ChatMessage
    ) {
        socketPublisher.publish(chatMessage.roomId, chatMessage)
    }
}
