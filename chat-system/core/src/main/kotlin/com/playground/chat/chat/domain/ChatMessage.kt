package com.playground.chat.chat.domain

import com.playground.chat.user.domain.User
import java.util.*

class ChatMessage(
    var id: UUID,
    var room: ChatRoom,
    var sender: User? = null,
    var type: ChatMessageType,
    var content: String,
)
