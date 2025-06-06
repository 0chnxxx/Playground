package com.playground.chat.channel.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.playground.chat.chat.data.event.ChatRoomEvent
import com.playground.chat.global.log.logger
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.stereotype.Component

@Component
class ChannelEventListener(
    private val mapper: ObjectMapper,
    private val eventPublisher: ApplicationEventPublisher
): MessageListener {
    private val log = logger()

    override fun onMessage(message: Message, pattern: ByteArray?) {
        try {
            log.info("[🛬 Chat Room Event Receive] event : {}", message)

            val event = mapper.readValue(message.toString(), ChatRoomEvent::class.java)

            eventPublisher.publishEvent(event)
        } catch (e: Exception) {
            log.error("[❌ Chat Room Event Receive Fail] {}", e.printStackTrace())
        }
    }
}
