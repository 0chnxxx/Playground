package com.playground.chat.channel.controller

import com.playground.chat.channel.service.ChannelService
import com.playground.chat.chat.data.event.SendChatMessageEvent
import com.playground.chat.chat.data.event.ReadChatMessageEvent
import com.playground.chat.global.auth.UserPrincipal
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class ChannelController(
    private val channelService: ChannelService
) {
    /**
     * 채팅 메시지 전송
     */
    @MessageMapping("/chat/rooms/{roomId}/messages/send")
    fun sendChatMessage(
        principal: UserPrincipal,
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
        principal: UserPrincipal,
        @DestinationVariable
        roomId: UUID,
        @DestinationVariable
        messageId: UUID,
        event: ReadChatMessageEvent
    ) {
        channelService.readChatMessage(principal, roomId, messageId, event)
    }
}
