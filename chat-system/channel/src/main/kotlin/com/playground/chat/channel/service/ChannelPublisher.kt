package com.playground.chat.channel.service

import com.playground.chat.channel.client.UserApiClient
import com.playground.chat.channel.global.kafka.CustomKafkaTemplate
import com.playground.chat.chat.data.event.ChatEventTopic
import com.playground.chat.chat.data.event.ReadChatMessageEvent
import com.playground.chat.chat.data.event.SendChatMessageEvent
import com.playground.chat.global.auth.CustomPrincipal
import com.playground.chat.global.log.logger
import com.playground.chat.global.util.IdUtil
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class ChannelPublisher(
    private val kafkaTemplate: CustomKafkaTemplate,
    private val redisTemplate: RedisTemplate<String, String>,
    private val userApiClient: UserApiClient
) {
    private val log = logger()

    fun publishChatMessageSendEvent(principal: CustomPrincipal?, event: SendChatMessageEvent) {
        try {
            var sendEvent = event.setMessageId(IdUtil.generateUuidV7())

            if (principal != null) {
                val response = userApiClient.findMe(principal.getBearerPassport())
                val user = response.data

                sendEvent = sendEvent.setSender(
                    userId = user.id,
                    image = user.image,
                    nickname = user.nickname,
                )
            }

            // ChatMessage Insert를 위한 Kafka Publish
            kafkaTemplate.send(
                principal,
                ChatEventTopic.CHAT_MESSAGE_SEND.withChannel(sendEvent.roomId.toString()),
                sendEvent
            )

            // Socket 에 BroadCast를 위한 Redis Publish
            redisTemplate.convertAndSend(
                ChatEventTopic.CHAT_MESSAGE_SEND.withChannel(sendEvent.roomId.toString()),
                sendEvent
            )

            log.info("[🛫 Chat Message Send Event Publish] {}", sendEvent)
        } catch (e: Exception) {
            log.error("[❌ Chat Message Send Event Publish Fail] {}", e.printStackTrace())
        }
    }

    fun publishChatMessageReadEvent(principal: CustomPrincipal?, event: ReadChatMessageEvent) {
        try {
            // Chat 의 LastMessage Update를 위한 Kafka Publish
            kafkaTemplate.send(
                principal,
                ChatEventTopic.CHAT_MESSAGE_READ.withChannel(event.roomId.toString()),
                event
            )

            // Socket 에 BroadCast를 위한 Redis Publish
            redisTemplate.convertAndSend(
                ChatEventTopic.CHAT_MESSAGE_READ.withChannel(event.roomId.toString()),
                event
            )

            log.info("[🛫 Chat Message Read Event Publish] {}", event)
        } catch (e: Exception) {
            log.error("[❌ Chat Message Read Event Publish Fail] {}", e.printStackTrace())
        }
    }
}
