package com.playground.chat.log

import com.playground.chat.dto.MessageDto
import com.playground.chat.event.ChatMessagePublishEvent
import com.playground.chat.event.ChatMessageSubscribeEvent
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectEvent
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent

@Component
class ChatLogger {
    val log = logger()

    @EventListener
    fun handleChatConnect(event: SessionConnectEvent) {
        val accessor = StompHeaderAccessor.wrap(event.message)

        log.info("🌐 Chat Connect Attempt: {}", accessor.sessionId)
    }

    @EventListener
    fun handleChatConnected(event: SessionConnectedEvent) {
        val accessor = StompHeaderAccessor.wrap(event.message)

        log.info("✅ Chat Connected: {}", accessor.sessionId)
    }

    @EventListener
    fun handleChatDisconnect(event: SessionDisconnectEvent) {
        val accessor = StompHeaderAccessor.wrap(event.message)

        log.info("❌ Chat Disconnected: {}", accessor.sessionId)
    }

    @EventListener
    fun handleChatMessagePublish(event: ChatMessagePublishEvent) {
        log.info("📨 Chat Message Publish: {}", event.messageDto)
    }

    @EventListener
    fun handleChatMessageSubscribe(event: ChatMessageSubscribeEvent) {
        log.info("📨 Chat Message Subscribe: {}", event.messageDto)
    }
}
