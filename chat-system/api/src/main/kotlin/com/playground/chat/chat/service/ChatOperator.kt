package com.playground.chat.chat.service

import com.playground.chat.chat.data.request.CreateChatRoomRequest
import com.playground.chat.chat.data.response.ChatRoomDto
import com.playground.chat.chat.domain.ChatMessage
import com.playground.chat.chat.domain.ChatRoom
import com.playground.chat.chat.entity.ChatEntity
import com.playground.chat.chat.entity.ChatMessageEntity
import com.playground.chat.chat.entity.ChatRoomEntity
import com.playground.chat.chat.repository.ChatRepository
import com.playground.chat.user.domain.User
import com.playground.chat.user.entity.UserEntity
import org.springframework.stereotype.Component
import java.util.*

@Component
class ChatOperator(
    private val chatFinder: ChatFinder,
    private val chatRepository: ChatRepository
) {
    fun createChatRoom(user: User, request: CreateChatRoomRequest): ChatRoomDto {
        val room = ChatRoomEntity(
            owner = UserEntity.fromUser(user),
            name = request.name
        )

        chatRepository.saveChatRoom(room)

        val chat = ChatEntity(
            user = UserEntity.fromUser(user),
            room = room
        )

        chatRepository.saveChat(chat)

        return ChatRoomDto(
            id = room.id!!,
            name = room.name,
            memberCount = 1,
            isJoined = true
        )
    }

    fun joinChatRoom(user: User, room: ChatRoom) {
        val chat = ChatEntity(
            user = UserEntity.fromUser(user),
            room = ChatRoomEntity.fromRoom(room)
        )

        chatRepository.saveChat(chat)
    }

    fun leaveChatRoom(user: User, room: ChatRoom) {
        chatRepository.deleteChatByUserAndRoom(
            user = UserEntity.fromUser(user),
            room = ChatRoomEntity.fromRoom(room)
        )
    }

    fun deleteChatRoom(room: ChatRoom) {
        chatRepository.deleteChatsByRoom(ChatRoomEntity.fromRoom(room))
        chatRepository.deleteChatRoom(ChatRoomEntity.fromRoom(room))
    }

    fun saveChatMessage(message: ChatMessage) {
        chatRepository.saveChatMessage(ChatMessageEntity.fromMessage(message))
    }

    fun readLastChatMessage(roomId: UUID, userId: UUID) {
        val lastChatMessage = chatFinder.findLastChatMessageByRoom(roomId)

        chatRepository.updateChatForLastMessage(roomId, userId, lastChatMessage.id)
    }

    fun readChatMessage(roomId: UUID, userId: UUID, messageId: UUID) {
        chatRepository.updateChatForLastMessage(roomId, userId, messageId)
    }
}
