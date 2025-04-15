package com.playground.chat.socket.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.playground.chat.chat.domain.ChatMessage
import com.playground.chat.global.util.logger
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class SocketEventMessageListener(
    private val mapper: ObjectMapper,
    private val messagingTemplate: SimpMessagingTemplate
): MessageListener {
    private val log = logger()

    override fun onMessage(message: Message, pattern: ByteArray?) {
        try {
            val body = message.body.toString(Charsets.UTF_8)
            val messageDto = mapper.readValue(body, ChatMessage::class.java)
            val destination = "/chat/rooms/${messageDto.roomId}"

            messagingTemplate.convertAndSend(destination, messageDto)
            log.info("[📨 Chat Message Receive] channel : {}, message : {}", messageDto.roomId, messageDto)
        } catch (e: Exception) {
            log.error("[❌ Chat Message Receive Fail] {}", e.printStackTrace())
        }
    }
}
