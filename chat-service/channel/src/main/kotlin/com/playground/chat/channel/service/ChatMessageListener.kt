package com.playground.chat.channel.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.playground.chat.chat.domain.ChatMessage
import com.playground.chat.global.log.logger
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class ChatMessageListener(
    private val mapper: ObjectMapper,
    private val messagingTemplate: SimpMessagingTemplate
): MessageListener {
    private val log = logger()

    override fun onMessage(message: Message, pattern: ByteArray?) {
        try {
            val body = message.body.toString(Charsets.UTF_8)
            val chatMessage = mapper.readValue(body, ChatMessage::class.java)
            val destination = "/chat/rooms/${chatMessage.roomId}"

            messagingTemplate.convertAndSend(destination, chatMessage)

            log.info("[📨 Chat Message Receive] channel : {}, message : {}", chatMessage.roomId, chatMessage)
        } catch (e: Exception) {
            log.error("[❌ Chat Message Receive Fail] {}", e.printStackTrace())
        }
    }
}
