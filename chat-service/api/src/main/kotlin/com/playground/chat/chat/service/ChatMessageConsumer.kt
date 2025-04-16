package com.playground.chat.chat.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.playground.chat.chat.domain.ChatMessage
import com.playground.chat.global.util.logger
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class ChatMessageConsumer(
    private val mapper: ObjectMapper
) {
    private val log = logger()

    @KafkaListener(
        topics = ["\${spring.kafka.channel.chat.topic}"],
        groupId = "chat-group",
        containerFactory = "chatKafkaListenerContainerFactory",
    )
    fun consume(message: String) {
        try {
            val chatMessage = mapper.readValue(message, ChatMessage::class.java)

            // TODO: DB 저장

            log.info("[📥 Chat Message Consume] message : {}", chatMessage)
        } catch (e: Exception) {
            log.error("[❌ Chat Message Consume Fail] {}", e.message)
        }
    }
}
