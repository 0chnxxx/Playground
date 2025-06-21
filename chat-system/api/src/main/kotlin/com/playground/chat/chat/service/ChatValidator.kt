package com.playground.chat.chat.service

import com.playground.chat.chat.data.exception.ChatErrorMessage
import com.playground.chat.chat.data.exception.ChatException
import com.playground.chat.chat.domain.ChatRoom
import com.playground.chat.chat.repository.ChatRepository
import com.playground.chat.user.data.exception.UserErrorMessage
import com.playground.chat.user.data.exception.UserException
import com.playground.chat.user.domain.User
import org.springframework.stereotype.Component

@Component
class ChatValidator(
    private val chatRepository: ChatRepository
) {
    fun checkRoomOwner(room: ChatRoom, user: User) {
        val owner = chatRepository.findChatRoomOwner(room.id!!)
            ?: throw UserException(UserErrorMessage.NOT_FOUND_USER)

        if (owner.id != user.id) {
            throw ChatException(ChatErrorMessage.FORBIDDEN_ROOM_OWNER)
        }
    }
}
