package com.playground.chat.socket.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.playground.chat.global.util.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class SocketPublisher(
    private val mapper: ObjectMapper,
    private val redisTemplate: RedisTemplate<String, String>,
    private val kafkaTemplate: KafkaTemplate<String, String>
) {
    private val log = logger()

    @Value("\${websocket.channel.prefix}")
    private lateinit var channel: String

    @Value("\${spring.kafka.chat.topic}")
    private lateinit var topic: String

    fun publish(roomId: String, message: Any) {
        try {
            val json = mapper.writeValueAsString(message)

            // Redis로 채팅 전송 메시지 발행
            redisTemplate.convertAndSend("${channel}:${roomId}", json)

            // Kafka로 채팅 저장 메시지 발행
            kafkaTemplate.send(topic, roomId, json)

            log.info("[✅ Chat Message Send] channel : {}, message : {}", roomId, json)
        } catch (e: Exception) {
            log.error("[❌ Chat Message Send Fail] {}", e.printStackTrace())
        }
    }
}
