package com.playground.chat.socket.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.playground.chat.global.util.logger
import com.playground.chat.socket.data.MessageDto
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class SocketListener(
    private val mapper: ObjectMapper,
    private val messagingTemplate: SimpMessagingTemplate
): MessageListener {
    private val log = logger()

    override fun onMessage(message: Message, pattern: ByteArray?) {
        try {
            val body = message.body.toString(Charsets.UTF_8)
            val messageDto = mapper.readValue(body, MessageDto::class.java)
            val destination = "/chat/rooms/${messageDto.roomId}"

            messagingTemplate.convertAndSend(destination, messageDto)
            log.info("[📨 Chat Message Receive] channel : {}, message : {}", messageDto.roomId, messageDto)
        } catch (e: Exception) {
            log.error("[❌ Chat Message Receive Fail] {}", e.printStackTrace())
        }
    }
}
