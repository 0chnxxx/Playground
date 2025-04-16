package com.playground.chat.channel.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.playground.chat.chat.data.event.ChatRoomEvent
import com.playground.chat.global.util.logger
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class ChatEventListener(
    private val mapper: ObjectMapper,
    private val chatSubscriber: ChatSubscriber
): MessageListener {
    private val log = logger()

    override fun onMessage(message: Message, pattern: ByteArray?) {
        try {
            val body = message.body.toString(Charsets.UTF_8)
            val event = mapper.readValue(body, ChatRoomEvent::class.java)

            when (event.type) {
                ChatRoomEvent.EventType.CREATE -> {
                    chatSubscriber.subscribeToRoom(event.userId.toString(), event.roomId.toString())
                }
                ChatRoomEvent.EventType.JOIN -> {
                    chatSubscriber.subscribeToRoom(event.userId.toString(), event.roomId.toString())
                }
                ChatRoomEvent.EventType.LEAVE -> {
                    chatSubscriber.unsubscribeToUserRoom(event.userId.toString(), event.roomId.toString())
                }
                ChatRoomEvent.EventType.DELETE -> {
                    chatSubscriber.unsubscribeToRoom(event.roomId.toString())
                }
            }

            log.info("[📨 Chat Event Receive] event : {}", event)
        } catch (e: Exception) {
            log.error("[❌ Chat Event Receive Fail] {}", e.printStackTrace())
        }
    }
}
