package com.playground.chat.channel.service

import com.playground.chat.chat.data.event.ChatRoomEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ChatEventHandler(
    private val chatSubscriber: ChatSubscriber
) {
    @EventListener
    fun handleChatRoomEvent(event: ChatRoomEvent) {
        when (event.type) {
            ChatRoomEvent.EventType.CREATE -> {
                chatSubscriber.subscribeToRoom(event.userId, event.roomId)
            }
            ChatRoomEvent.EventType.JOIN -> {
                chatSubscriber.subscribeToRoom(event.userId, event.roomId)
            }
            ChatRoomEvent.EventType.LEAVE -> {
                chatSubscriber.unsubscribeToUserRoom(event.userId, event.roomId)
            }
            ChatRoomEvent.EventType.DELETE -> {
                chatSubscriber.unsubscribeToRoom(event.roomId)
            }
        }
    }
}
