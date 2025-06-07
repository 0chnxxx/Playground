package com.playground.chat.chat.service

import com.playground.chat.chat.data.event.ChatEventTopic
import com.playground.chat.chat.data.event.ChatRoomEvent
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
            redisTemplate.convertAndSend(ChatEventTopic.CHAT_ROOM_EVENT.topic, event)

            log.info("[✅ Chat Room Event Publish] {}", event)
        } catch (e: Exception) {
            log.error("[❌ Chat Room Event Publish Fail] {}", e.printStackTrace())
        }
    }
}
