package com.playground.chat.channel.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.playground.chat.channel.client.UserApiClient
import com.playground.chat.chat.data.event.ReadChatMessageEvent
import com.playground.chat.chat.data.event.SendChatMessageEvent
import com.playground.chat.global.auth.CustomPrincipal
import com.playground.chat.global.log.logger
import com.playground.chat.global.util.UuidUtil
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class ChannelPublisher(
    private val mapper: ObjectMapper,
    private val redisTemplate: RedisTemplate<String, String>,
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val userApiClient: UserApiClient
) {
    private val log = logger()

    fun publishChatMessageSendEvent(principal: CustomPrincipal?, event: SendChatMessageEvent) {
        try {
            var sendEvent = event.setMessageId(UuidUtil.generateUuidV7())

            if (principal != null) {
                val response = userApiClient.findMe(principal.getBearerPassport())
                val user = response.data

                sendEvent = sendEvent.setSender(
                    userId = user.id,
                    image = user.image,
                    nickname = user.nickname,
                )
            }

            val sendEventJson = mapper.writeValueAsString(sendEvent)

            // ChatMessage Insert를 위한 Kafka publish
            kafkaTemplate.send("chat-message-send", sendEvent.roomId.toString(), sendEventJson)

            // Socket 에 BroadCast를 위한 Redis Publish
            redisTemplate.convertAndSend("chat-message-send:${sendEvent.roomId}", sendEvent)

            log.info("[🛫 Chat Message Send Event Publish] {}", sendEvent)
        } catch (e: Exception) {
            log.error("[❌ Chat Message Send Event Publish Fail] {}", e.printStackTrace())
        }
    }

    fun publishChatMessageReadEvent(principal: CustomPrincipal?, event: ReadChatMessageEvent) {
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
