package com.playground.chat.channel.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.playground.chat.chat.data.event.SendChatMessageEvent
import com.playground.chat.global.log.logger
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class ChannelSendListener(
    private val mapper: ObjectMapper,
    private val messagingTemplate: SimpMessagingTemplate
): MessageListener {
    private val log = logger()

    override fun onMessage(message: Message, pattern: ByteArray?) {
        try {
            log.info("[🛬 Chat Message Send Event Receive] {}", message)

            val body = message.body.toString(Charsets.UTF_8)
            val event = mapper.readValue(body, SendChatMessageEvent::class.java)

            messagingTemplate.convertAndSend("/sub/chat/rooms/${event.roomId}/messages/send", event)
        } catch (e: Exception) {
            log.error("[❌ Chat Message Send Event Receive Fail] {}", e.printStackTrace())
        }
    }
}
