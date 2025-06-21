package com.playground.chat.chat.domain

import com.playground.chat.user.domain.User
import java.time.Instant
import java.util.*

class Chat(
    var user: User,
    var room: ChatRoom,
    var lastMessageId: UUID? = null,
    var lastReadAt: Instant? = null,
    var joinedAt: Instant = Instant.now()
)
