package com.playground.chat.event

import com.playground.chat.dto.MessageDto

data class ChatMessageSubscribeEvent(
    val messageDto: MessageDto
)
