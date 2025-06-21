package com.playground.chat.channel.service

import com.playground.chat.chat.data.event.ReadChatMessageEvent
import com.playground.chat.chat.data.event.SendChatMessageEvent
import com.playground.chat.global.security.CustomPrincipal
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChannelService(
    private val channelPublisher: ChannelPublisher
) {
    fun sendChatMessage(principal: CustomPrincipal, roomId: UUID, event: SendChatMessageEvent) {
        channelPublisher.publishChatMessageSendEvent(principal, event)
    }

    fun readChatMessage(principal: CustomPrincipal, roomId: UUID, messageId: UUID, event: ReadChatMessageEvent) {
        channelPublisher.publishChatMessageReadEvent(principal, event)
    }
}
