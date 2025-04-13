package com.playground.chat.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.playground.chat.dto.MessageDto
import com.playground.chat.event.ChatMessageSubscribeEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class ChatSubscriber(
    private val objectMapper: ObjectMapper,
    private val messagingTemplate: SimpMessagingTemplate,
    private val eventPublisher: ApplicationEventPublisher
): MessageListener {
    override fun onMessage(message: Message, pattern: ByteArray?) {
        val body = message.body.toString(Charsets.UTF_8)
        val dto = objectMapper.readValue(body, MessageDto::class.java)

        messagingTemplate.convertAndSend("/sub/chat/rooms/${dto.roomId}", dto)
        eventPublisher.publishEvent(ChatMessageSubscribeEvent(dto))
    }
}
