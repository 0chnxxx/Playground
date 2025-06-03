package com.playground.chat.chat.service

import com.playground.chat.chat.data.request.CreateChatRoomRequest
import com.playground.chat.chat.data.response.ChatRoomDto
import com.playground.chat.chat.entity.ChatEntity
import com.playground.chat.chat.entity.ChatMessageEntity
import com.playground.chat.chat.entity.ChatRoomEntity
import com.playground.chat.chat.repository.ChatRepository
import com.playground.chat.user.entity.UserEntity
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ChatOperator(
    private val chatRepository: ChatRepository
) {
    fun createChatRoom(user: UserEntity, request: CreateChatRoomRequest): ChatRoomDto {
        val room = ChatRoomEntity(
            owner = user,
            name = request.name
        )

        chatRepository.saveChatRoom(room)

        val chat = ChatEntity(
            user = user,
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

    fun joinChatRoom(user: UserEntity, room: ChatRoomEntity) {
        val chat = ChatEntity(
            user = user,
            room = room
        )

        chatRepository.saveChat(chat)
    }

    fun leaveChatRoom(user: UserEntity, room: ChatRoomEntity) {
        chatRepository.deleteChatByUserAndRoom(user, room)
    }

    fun deleteChatRoom(room: ChatRoomEntity) {
        chatRepository.deleteChatsByRoom(room)
        chatRepository.deleteChatRoom(room)
    }

    fun saveChatMessage(message: ChatMessageEntity) {
        chatRepository.saveChatMessage(message)
    }

    fun readLastChatMessage(roomId: UUID, userId: UUID) {
        chatRepository.updateChatForLastMessage(roomId, userId)
    }

    fun readChatMessage(roomId: UUID, userId: UUID, messageId: UUID) {
        chatRepository.updateChatForMessage(roomId, userId, messageId)
    }
}
