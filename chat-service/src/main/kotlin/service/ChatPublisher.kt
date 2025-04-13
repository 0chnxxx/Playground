package com.playground.chat.service

import com.playground.chat.dto.MessageDto
import com.playground.chat.event.ChatMessagePublishEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class ChatPublisher(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val eventPublisher: ApplicationEventPublisher
) {
    fun publish(messageDto: MessageDto) {
        redisTemplate.convertAndSend("chat:" + messageDto.roomId, messageDto)
        eventPublisher.publishEvent(ChatMessagePublishEvent(messageDto))
    }
}
