package com.playground.chat.event

import com.playground.chat.dto.MessageDto

data class ChatMessagePublishEvent(
    val messageDto: MessageDto
)
