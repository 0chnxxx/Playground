package com.playground.chat.channel.global.log

import com.fasterxml.jackson.databind.ObjectMapper
import com.playground.chat.global.log.logger
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.stereotype.Component
import java.util.Base64

@Component
class AccessLogInterceptor(
    private val mapper: ObjectMapper,
): ChannelInterceptor {
    private val log = logger()

    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
        val accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)

        if (accessor != null && accessor.command == StompCommand.SEND) {
            val sessionId = accessor.sessionId
            val destination = accessor.destination
            val encodedPayload = mapper.writeValueAsString(message.payload)
//            val decodedPayload = Base64.getDecoder().decode(encodedPayload)

            log.info(
                "[STOMP SEND] sessionId={}, destination={}, payload={}",
                sessionId,
                destination,
                encodedPayload
            )
        }

        return message
    }
}
