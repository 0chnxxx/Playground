package com.playground.chat.chat.service

import com.playground.chat.chat.data.event.*
import com.playground.chat.global.log.logger
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class ChatPublisher(
    private val redisTemplate: RedisTemplate<String, String>
) {
    private val log = logger()

    fun publishChatRoomEvent(event: ChatRoomEvent) {
        try {
            redisTemplate.convertAndSend("chat-room-event", event)

            log.info("[✅ Chat Room Event Publish] {}", event)
        } catch (e: Exception) {
            log.error("[❌ Chat Room Event Publish Fail] {}", e.printStackTrace())
        }
    }

    fun publishChatMessageSendEvent(event: SendChatMessageEvent) {
        try {
            redisTemplate.convertAndSend("chat-message-send:${event.roomId}", event)

            log.info("[✅ Chat Message Send Event Publish] {}", event)
        } catch (e: Exception) {
            log.error("[❌ Chat Message Send Event Publish Fail] {}", e.printStackTrace())
        }
    }

    fun publishChatMessageReadEvent(event: ReadChatMessageEvent) {
        try {
            redisTemplate.convertAndSend("chat-message-read:${event.roomId}", event)

            log.info("[✅ Chat Message Read Event Publish] {}", event)
        } catch (e: Exception) {
            log.error("[❌ Chat Message Read Event Publish Fail] {}", e.printStackTrace())
        }
    }
}
