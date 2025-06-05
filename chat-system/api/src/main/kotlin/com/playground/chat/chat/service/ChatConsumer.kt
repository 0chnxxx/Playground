package com.playground.chat.chat.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.playground.chat.chat.data.event.ReadChatMessageEvent
import com.playground.chat.chat.data.event.SendChatMessageEvent
import com.playground.chat.chat.data.event.ViewChatRoomEvent
import com.playground.chat.chat.entity.ChatMessageEntity
import com.playground.chat.global.log.logger
import com.playground.chat.user.service.UserFinder
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ChatConsumer(
    private val mapper: ObjectMapper,
    private val userFinder: UserFinder,
    private val chatFinder: ChatFinder,
    private val chatOperator: ChatOperator
) {
    private val log = logger()

    @Transactional
    @KafkaListener(
        topics = ["chat-room-view"],
        groupId = "chat-group",
        containerFactory = "chatKafkaListenerContainerFactory",
    )
    fun consumeChatRoomView(event: String) {
        try {
            val message = mapper.readValue(event, ViewChatRoomEvent::class.java)

            val lastMessage = chatFinder.findLastChatMessage(message.roomId, message.userId)

            if (lastMessage != null && lastMessage.id != message.messageId) {
                chatOperator.readLastChatMessage(message.roomId, message.userId)
            }

            log.info("[📥 Chat Room View Event Consume] message : {}", event)
        } catch (e: Exception) {
            log.error("[❌ Chat Room View Event Consume Fail] {}", e.message)
        }
    }

    @Transactional
    @KafkaListener(
        topics = ["chat-message-send"],
        groupId = "chat-group",
        containerFactory = "chatKafkaListenerContainerFactory",
    )
    fun consumeChatMessageSend(event: String) {
        try {
            val sendEvent = mapper.readValue(event, SendChatMessageEvent::class.java)

            val user = userFinder.findUser(sendEvent.userId)
            val room = chatFinder.findChatRoom(sendEvent.roomId)

            val message = ChatMessageEntity(
                id = sendEvent.messageId,
                room = room,
                sender = user,
                content = sendEvent.content
            )

            chatOperator.saveChatMessage(message)

            log.info("[📥 Chat Message Send Event Consume] message : {}", event)
        } catch (e: Exception) {
            log.error("[❌ Chat Message Send Event Consume Fail] {}", e.message)
        }
    }

    @Transactional
    @KafkaListener(
        topics = ["chat-message-read"],
        groupId = "chat-group",
        containerFactory = "chatKafkaListenerContainerFactory",
    )
    fun consumeChatMessageRead(event: String) {
        try {
            val message = mapper.readValue(event, ReadChatMessageEvent::class.java)

            chatOperator.readChatMessage(message.roomId, message.userId, message.messageId)

            log.info("[📥 Chat Message Read Event Consume] message : {}", event)
        } catch (e: Exception) {
            log.error("[❌ Chat Message Read Event Consume Fail] {}", e.message)
        }
    }
}
