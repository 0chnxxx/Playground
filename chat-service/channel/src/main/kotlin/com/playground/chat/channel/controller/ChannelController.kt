package com.playground.chat.channel.controller

import com.playground.chat.channel.service.ChannelService
import com.playground.chat.chat.data.response.ChatMessageDto
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
        message: ChatMessageDto
    ) {
        channelService.sendChatMessage(message.roomId, message)
    }
}
