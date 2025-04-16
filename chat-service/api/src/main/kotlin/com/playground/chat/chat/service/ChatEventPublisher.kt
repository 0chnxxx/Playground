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

    @Value("\${spring.redis.channel.chat-event.prefix}")
    private lateinit var channel: String

    fun publish(roomId: String, message: Any) {
        try {
            val json = mapper.writeValueAsString(message)

            // Redis로 채팅 이벤트 발행
            redisTemplate.convertAndSend("${channel}:${roomId}", json)

            log.info("[✅ Chat Event Send] channel : {}, event : {}", roomId, json)
        } catch (e: Exception) {
            log.error("[❌ Chat Event Send Fail] {}", e.printStackTrace())
        }
    }
}
