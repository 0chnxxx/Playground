package com.playground.chat.chat.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.playground.chat.global.util.logger
import com.playground.chat.socket.data.MessageDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class ChatConsumer(
    private val mapper: ObjectMapper
) {
    private val log = logger()

    @KafkaListener(
        topics = ["\${spring.kafka.chat.topic}"],
        groupId = "chat-group",
        containerFactory = "chatKafkaListenerContainerFactory",
    )
    fun consume(message: String) {
        try {
            val chatMessage = mapper.readValue(message, MessageDto::class.java)

            // TODO: DB 저장

            log.info("[📥 Kafka Consume] message : {}", chatMessage)
        } catch (e: Exception) {
            log.error("[❌ Kafka Consume Fail] {}", e.message)
        }
    }
}
