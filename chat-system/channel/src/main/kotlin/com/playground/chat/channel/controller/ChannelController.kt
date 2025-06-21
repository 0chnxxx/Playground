package com.playground.chat.channel.controller

import com.playground.chat.channel.service.ChannelService
import com.playground.chat.chat.data.event.ReadChatMessageEvent
import com.playground.chat.chat.data.event.SendChatMessageEvent
import com.playground.chat.global.security.CustomPrincipal
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class ChannelController(
    private val channelService: ChannelService
) {
    /**
     * 채팅 메시지 전송
     */
    @MessageMapping("/chat/rooms/{roomId}/messages/send")
    fun sendChatMessage(
        principal: CustomPrincipal,
        @DestinationVariable
        roomId: UUID,
        event: SendChatMessageEvent
    ) {
        channelService.sendChatMessage(principal, roomId, event)
    }

    /**
     * 채팅 메시지 읽음
     */
    @MessageMapping("/chat/rooms/{roomId}/messages/{messageId}/read")
    fun readChatMessage(
        principal: CustomPrincipal,
        @DestinationVariable
        roomId: UUID,
        @DestinationVariable
        messageId: UUID,
        event: ReadChatMessageEvent
    ) {
        channelService.readChatMessage(principal, roomId, messageId, event)
    }
}
