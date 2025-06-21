package com.playground.chat.chat.domain

import com.playground.chat.user.domain.User
import java.util.*

class ChatRoom(
    var id: UUID? = null,
    var owner: User,
    var image: String? = null,
    var name: String,
    var chats: MutableList<Chat> = mutableListOf(),
    var users: MutableList<User> = mutableListOf(),
    var messages: MutableList<ChatMessage> = mutableListOf(),
) {
    fun isOwner(userId: UUID): Boolean {
        return owner.id == userId
    }
}
