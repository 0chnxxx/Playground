package com.playground.chat.channel.controller

import com.playground.chat.channel.service.ChannelService
import com.playground.chat.chat.data.event.SendChatMessageEvent
import com.playground.chat.chat.data.event.ReadChatMessageEvent
import com.playground.chat.chat.data.event.ViewChatRoomEvent
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
     * 채팅방 접속
     */
    @MessageMapping("/chat/rooms/{roomId}/view")
    fun viewChatRoom(
        @DestinationVariable
        roomId: UUID,
        principal: UserPrincipal,
        event: ViewChatRoomEvent
    ) {
        channelService.viewChatRoom(principal.id, roomId, event)
    }

    /**
     * 채팅 메시지 전송
     */
    @MessageMapping("/chat/rooms/{roomId}/messages/send")
    fun sendChatMessage(
        @DestinationVariable
        roomId: UUID,
        principal: UserPrincipal,
        event: SendChatMessageEvent
    ) {
        channelService.sendChatMessage(principal.id, roomId, event)
    }

    /**
     * 채팅 메시지 읽음
     */
    @MessageMapping("/chat/rooms/{roomId}/messages/{messageId}/read")
    fun readChatMessage(
        @DestinationVariable
        roomId: UUID,
        @DestinationVariable
        messageId: UUID,
        principal: UserPrincipal,
        event: ReadChatMessageEvent
    ) {
        channelService.readChatMessage(principal.id, roomId, messageId, event)
    }
}
