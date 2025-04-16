package com.playground.chat.channel.controller

import com.playground.chat.channel.service.ChannelService
import com.playground.chat.chat.domain.ChatMessage
import com.playground.chat.channel.service.ChatMessagePublisher
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ChannelController(
    private val channelService: ChannelService
) {
    /**
     * 채팅 메시지 전송
     */
    @MessageMapping("/chat/rooms/{roomId}/messages/send")
    fun sendChatMessage(
        message: ChatMessage
    ) {
        channelService.sendChatMessage(message.roomId, message)
    }
}
