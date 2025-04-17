package com.playground.chat.chat.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.playground.chat.chat.data.response.ChatMessageDto
import com.playground.chat.chat.entity.ChatMessageEntity
import com.playground.chat.chat.entity.ChatRoomEntity
import com.playground.chat.global.log.logger
import com.playground.chat.user.entity.UserEntity
import jakarta.persistence.EntityManager
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ChatMessageConsumer(
    private val mapper: ObjectMapper,
    private val entityManager: EntityManager,
    private val chatOperator: ChatOperator
) {
    private val log = logger()

    @Transactional
    @KafkaListener(
        topics = ["\${spring.kafka.channel.chat.topic}"],
        groupId = "chat-group",
        containerFactory = "chatKafkaListenerContainerFactory",
    )
    fun consume(message: String) {
        try {
            val chatMessageDto = mapper.readValue(message, ChatMessageDto::class.java)

            val roomProxy = entityManager.getReference(ChatRoomEntity::class.java, chatMessageDto.roomId)
            val userProxy = entityManager.getReference(UserEntity::class.java, chatMessageDto.userId)

            chatOperator.saveChatMessage(
                ChatMessageEntity(
                    room = roomProxy,
                    sender = userProxy,
                    content = chatMessageDto.content
                )
            )

            log.info("[📥 Chat Message Consume] message : {}", chatMessageDto)
        } catch (e: Exception) {
            log.error("[❌ Chat Message Consume Fail] {}", e.message)
        }
    }
}
