package com.playground.chat.channel.service

import com.playground.chat.chat.data.event.SendChatMessageEvent
import com.playground.chat.chat.data.event.ReadChatMessageEvent
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ChannelService(
    private val channelPublisher: ChannelPublisher
) {
    fun viewChatRoom(userId: UUID, roomId: UUID, event: ViewChatRoomEvent) {
        channelPublisher.publishChatMessageViewEvent(event)
    }

    fun sendChatMessage(userId: UUID, roomId: UUID, event: SendChatMessageEvent) {
        channelPublisher.publishChatMessageSendEvent(event)
    }

    fun readChatMessage(userId: UUID, roomId: UUID, messageId: UUID, event: ReadChatMessageEvent) {
        channelPublisher.publishChatMessageReadEvent(event)
    }
}
