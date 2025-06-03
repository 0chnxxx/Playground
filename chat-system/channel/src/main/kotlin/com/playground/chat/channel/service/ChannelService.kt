package com.playground.chat.channel.service

import com.playground.chat.chat.data.event.SendChatMessageEvent
import com.playground.chat.chat.data.event.ReadChatMessageEvent
import com.playground.chat.chat.data.event.ViewChatRoomEvent
import org.springframework.stereotype.Service

@Service
class ChannelService(
    private val channelPublisher: ChannelPublisher
) {
    fun viewChatRoom(userId: Long, roomId: Long, event: ViewChatRoomEvent) {
        channelPublisher.publishChatMessageViewEvent(event)
    }

    fun sendChatMessage(userId: Long, roomId: Long, event: SendChatMessageEvent) {
        channelPublisher.publishChatMessageSendEvent(event)
    }

    fun readChatMessage(userId: Long, roomId: Long, event: ReadChatMessageEvent) {
        channelPublisher.publishChatMessageReadEvent(event)
    }
}
