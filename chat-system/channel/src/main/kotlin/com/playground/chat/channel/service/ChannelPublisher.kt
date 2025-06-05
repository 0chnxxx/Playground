package com.playground.chat.channel.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.playground.chat.chat.data.event.*
import com.playground.chat.global.log.logger
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class ChannelPublisher(
    private val mapper: ObjectMapper,
    private val redisTemplate: RedisTemplate<String, String>,
    private val kafkaTemplate: KafkaTemplate<String, String>
) {
    private val log = logger()

    fun publishChatMessageSendEvent(event: SendChatMessageEvent) {
        try {
            val sendEventJson = mapper.writeValueAsString(event)

            // ChatMessage Insert를 위한 Kafka publish
            kafkaTemplate.send("chat-message-send", event.roomId.toString(), sendEventJson)

            // Socket 에 BroadCast를 위한 Redis Publish
            redisTemplate.convertAndSend("chat-message-send:${event.roomId}", event)

            log.info("[🛫 Chat Message Send Event Publish] {}", event)
        } catch (e: Exception) {
            log.error("[❌ Chat Message Send Event Publish Fail] {}", e.printStackTrace())
        }
    }

    fun publishChatMessageReadEvent(event: ReadChatMessageEvent) {
        try {
            val readEventJson = mapper.writeValueAsString(event)

            // Chat 의 LastMessage Update를 위한 Kafka publish
            kafkaTemplate.send("chat-message-read", event.roomId.toString(), readEventJson)

            // Socket 에 BroadCast를 위한 Redis Publish
            redisTemplate.convertAndSend("chat-message-read:${event.roomId}", event)

            log.info("[🛫 Chat Message Read Event Publish] {}", event)
        } catch (e: Exception) {
            log.error("[❌ Chat Message Read Event Publish Fail] {}", e.printStackTrace())
        }
    }
}
