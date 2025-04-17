package com.playground.chat.channel.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.playground.chat.global.log.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class ChatMessagePublisher(
    private val mapper: ObjectMapper,
    private val redisTemplate: RedisTemplate<String, String>,
    private val kafkaTemplate: KafkaTemplate<String, String>
) {
    private val log = logger()

    @Value("\${spring.data.redis.channel.chat-message.topic}")
    private lateinit var channel: String

    @Value("\${spring.kafka.channel.chat.topic}")
    private lateinit var topic: String

    fun publish(roomId: Long, message: Any) {
        try {
            val json = mapper.writeValueAsString(message)

            // Redis로 채팅 메시지 발행
            redisTemplate.convertAndSend("${channel}:${roomId}", message)

            // Kafka로 채팅 메시지 발행
            kafkaTemplate.send(topic, roomId.toString(), json)

            log.info("[✅ Chat Message Send] channel : {}, message : {}", roomId, json)
        } catch (e: Exception) {
            log.error("[❌ Chat Message Send Fail] {}", e.printStackTrace())
        }
    }
}
