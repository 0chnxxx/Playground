package com.playground.chat.channel.service

import com.playground.chat.chat.data.event.SendChatMessageEvent
import com.playground.chat.chat.data.event.ReadChatMessageEvent
import com.playground.chat.global.auth.UserPrincipal
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ChannelService(
    private val channelPublisher: ChannelPublisher
) {
    fun sendChatMessage(principal: UserPrincipal, roomId: UUID, event: SendChatMessageEvent) {
        channelPublisher.publishChatMessageSendEvent(principal, event)
    }

    fun readChatMessage(principal: UserPrincipal, roomId: UUID, messageId: UUID, event: ReadChatMessageEvent) {
        channelPublisher.publishChatMessageReadEvent(principal, event)
    }
}
