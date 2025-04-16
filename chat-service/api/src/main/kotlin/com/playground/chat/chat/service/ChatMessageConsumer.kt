package com.playground.chat.chat.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.playground.chat.chat.domain.ChatMessage
import com.playground.chat.chat.entity.ChatMessageEntity
import com.playground.chat.chat.entity.ChatRoomEntity
import com.playground.chat.global.log.logger
import com.playground.chat.user.entity.UserEntity
import jakarta.persistence.EntityManager
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class ChatMessageConsumer(
    private val mapper: ObjectMapper,
    private val entityManager: EntityManager,
    private val chatOperator: ChatOperator
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

            val roomProxy = entityManager.getReference(ChatRoomEntity::class.java, chatMessage.roomId.toLong())
            val userProxy = entityManager.getReference(UserEntity::class.java, chatMessage.sender.userId.toLong())

            chatOperator.saveChatMessage(
                ChatMessageEntity(
                    room = roomProxy,
                    sender = userProxy,
                    content = chatMessage.content
                )
            )

            log.info("[📥 Chat Message Consume] message : {}", chatMessage)
        } catch (e: Exception) {
            log.error("[❌ Chat Message Consume Fail] {}", e.message)
        }
    }
}
