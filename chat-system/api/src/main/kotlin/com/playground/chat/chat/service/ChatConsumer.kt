package com.playground.chat.chat.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.playground.chat.chat.data.event.ReadChatMessageEvent
import com.playground.chat.chat.data.event.SendChatMessageEvent
import com.playground.chat.chat.entity.ChatMessageEntity
import com.playground.chat.chat.entity.ChatMessageType
import com.playground.chat.global.auth.PrincipalContext
import com.playground.chat.global.auth.UserPrincipal
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
        topics = ["chat-message-send"],
        groupId = "chat-group",
        containerFactory = "chatKafkaListenerContainerFactory",
    )
    fun consumeSendChatMessageEvent(event: String) {
        try {
            val sendEvent = mapper.readValue(event, SendChatMessageEvent::class.java)

            val room = chatFinder.findChatRoom(sendEvent.roomId)
            val user = sendEvent.userId?.let { userFinder.findUser(it) }

            val message = ChatMessageEntity(
                id = sendEvent.messageId!!,
                room = room,
                sender = user,
                type = ChatMessageType.valueOf(sendEvent.type),
                content = sendEvent.content
            )

            if (user != null) {
                val principal = UserPrincipal(user.id!!)

                PrincipalContext.operate(principal) {
                    chatOperator.saveChatMessage(message)
                }
            } else {
                chatOperator.saveChatMessage(message)
            }

            log.info("[📨 Chat Message Send Event Consume] {}", event)
        } catch (e: Exception) {
            log.error("[❌ Chat Message Send Event Consume Fail] {}", e.printStackTrace())
        }
    }

    @Transactional
    @KafkaListener(
        topics = ["chat-message-read"],
        groupId = "chat-group",
        containerFactory = "chatKafkaListenerContainerFactory",
    )
    fun consumeReadChatMessageEvent(event: String) {
        try {
            val readEvent = mapper.readValue(event, ReadChatMessageEvent::class.java)

            val principal = UserPrincipal(readEvent.userId)

            PrincipalContext.operate(principal) {
                when (readEvent.type) {
                    ReadChatMessageEvent.Type.ALL -> chatOperator.readLastChatMessage(
                        roomId = readEvent.roomId,
                        userId = readEvent.userId
                    )

                    ReadChatMessageEvent.Type.ONE -> chatOperator.readChatMessage(
                        roomId = readEvent.roomId,
                        userId = readEvent.userId,
                        messageId = readEvent.messageId
                    )
                }
            }

            log.info("[📨 Chat Message Read Event Consume] {}", event)
        } catch (e: Exception) {
            log.error("[❌ Chat Message Read Event Consume Fail] {}", e.printStackTrace())
        }
    }
}
