package com.playground.chat.channel.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.playground.chat.chat.data.event.*
import com.playground.chat.global.log.logger
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class ChannelPublisher(
    private val mapper: ObjectMapper,
    private val kafkaTemplate: KafkaTemplate<String, String>
) {
    private val log = logger()

    fun publishChatRoomCreateEvent(event: CreateChatRoomEvent) {
        TODO("Not yet implemented")
    }

    fun publishChatRoomJoinEvent(event: JoinChatRoomEvent) {
        TODO("Not yet implemented")
    }

    fun publishChatRoomLeaveEvent(event: LeaveChatRoomEvent) {
        TODO("Not yet implemented")
    }

    fun publishChatRoomDeleteEvent(event: DeleteChatRoomEvent) {
        TODO("Not yet implemented")
    }

    fun publishChatMessageViewEvent(event: ViewChatRoomEvent) {
        try {
            val viewEventJson = mapper.writeValueAsString(event)

            kafkaTemplate.send("chat-room-view", event.roomId.toString(), viewEventJson)

            log.info("[✅ Chat Room View Event Publish] {}", event)
        } catch (e: Exception) {
            log.error("[❌ Chat Room View Event Publish Fail] {}", e.printStackTrace())
        }
    }

    fun publishChatMessageSendEvent(event: SendChatMessageEvent) {
        try {
            val json = mapper.writeValueAsString(event)

            kafkaTemplate.send("chat-message-send", event.roomId.toString(), json)

            log.info("[✅ Chat Message Send Event Publish] {}", event)
        } catch (e: Exception) {
            log.error("[❌ Chat Message Send Event Publish Fail] {}", e.printStackTrace())
        }
    }

    fun publishChatMessageReadEvent(event: ReadChatMessageEvent) {
        try {
            val json = mapper.writeValueAsString(event)

            kafkaTemplate.send("chat-message-read", event.roomId.toString(), json)

            log.info("[✅ Chat Message Read Event Publish] {}", event)
        } catch (e: Exception) {
            log.error("[❌ Chat Message Read Event Publish Fail] {}", e.printStackTrace())
        }
    }
}
