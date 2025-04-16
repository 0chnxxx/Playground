package com.playground.chat.chat.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.playground.chat.global.util.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class ChatEventPublisher(
    private val mapper: ObjectMapper,
    private val redisTemplate: RedisTemplate<String, String>
) {
    private val log = logger()

    @Value("\${spring.data.redis.channel.chat-event.topic}")
    private lateinit var channel: String

    fun publish(roomId: String, event: Any) {
        try {
            val json = mapper.writeValueAsString(event)

            // Redis로 채팅 이벤트 발행
            redisTemplate.convertAndSend(channel, json)

            log.info("[✅ Chat Event Send] event : {}", json)
        } catch (e: Exception) {
            log.error("[❌ Chat Event Send Fail] {}", e.printStackTrace())
        }
    }
}
