package com.playground.chat.channel.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.playground.chat.chat.data.response.ChatMessageDto
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
            val chatMessageDto = mapper.readValue(body, ChatMessageDto::class.java)
            val destination = "/chat/rooms/${chatMessageDto.roomId}"

            messagingTemplate.convertAndSend(destination, chatMessageDto)

            log.info("[📨 Chat Message Receive] channel : {}, message : {}", chatMessageDto.roomId, chatMessageDto)
        } catch (e: Exception) {
            log.error("[❌ Chat Message Receive Fail] {}", e.printStackTrace())
        }
    }
}
