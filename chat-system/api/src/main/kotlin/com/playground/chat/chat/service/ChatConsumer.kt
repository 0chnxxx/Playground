package com.playground.chat.chat.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.playground.chat.chat.data.event.ReadChatMessageEvent
import com.playground.chat.chat.data.event.SendChatMessageEvent
import com.playground.chat.chat.entity.ChatMessageEntity
import com.playground.chat.chat.entity.ChatMessageType
import com.playground.chat.global.auth.CustomPrincipal
import com.playground.chat.global.auth.PrincipalContext
import com.playground.chat.global.auth.PrincipalRole
import com.playground.chat.global.log.logger
import com.playground.chat.user.service.UserFinder
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager

@Component
class ChatConsumer(
    private val mapper: ObjectMapper,
    private val userFinder: UserFinder,
    private val chatFinder: ChatFinder,
    private val chatOperator: ChatOperator
) {
    private val log = logger()

    @Transactional
    @KafkaListener(topics = ["chat-message-send"], concurrency = "3")
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
                val principal = CustomPrincipal(user.id!!, PrincipalRole.USER)

                PrincipalContext.operate(
                    principal = principal,
                    function = {
                        chatOperator.saveChatMessage(message)
                    },
                    after = {
                        TransactionSynchronizationManager.registerSynchronization(object: TransactionSynchronization {
                            override fun afterCompletion(status: Int) {
                                PrincipalContext.clear()
                            }
                        })
                    }
                )
            } else {
                chatOperator.saveChatMessage(message)
            }

            log.info("[📨 Chat Message Send Event Consume] {}", event)
        } catch (e: Exception) {
            log.error("[❌ Chat Message Send Event Consume Fail] {}", e.printStackTrace())
        }
    }

    @Transactional
    @KafkaListener(topics = ["chat-message-read"], concurrency = "3")
    fun consumeReadChatMessageEvent(event: String) {
        try {
            val readEvent = mapper.readValue(event, ReadChatMessageEvent::class.java)

            val principal = CustomPrincipal(readEvent.userId, PrincipalRole.USER)

            PrincipalContext.operate(
                principal = principal,
                function = {
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
                },
                after = {
                    TransactionSynchronizationManager.registerSynchronization(object: TransactionSynchronization {
                        override fun afterCompletion(status: Int) {
                            PrincipalContext.clear()
                        }
                    })
                }
            )

            log.info("[📨 Chat Message Read Event Consume] {}", event)
        } catch (e: Exception) {
            log.error("[❌ Chat Message Read Event Consume Fail] {}", e.printStackTrace())
        }
    }
}
