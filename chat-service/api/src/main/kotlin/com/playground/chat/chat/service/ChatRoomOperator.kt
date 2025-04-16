package com.playground.chat.chat.service

import com.playground.chat.chat.data.CreateChatRoomRequest
import com.playground.chat.chat.entity.ChatEntity
import com.playground.chat.chat.entity.ChatRoomEntity
import com.playground.chat.chat.repository.ChatRepository
import com.playground.chat.chat.repository.ChatRoomRepository
import com.playground.chat.user.entity.UserEntity
import org.springframework.stereotype.Component

@Component
class ChatRoomOperator(
    private val chatRepository: ChatRepository,
    private val chatRoomRepository: ChatRoomRepository
) {
    fun createChatRoom(user: UserEntity, request: CreateChatRoomRequest): ChatRoomEntity {
        val room = ChatRoomEntity(
            owner = user,
            name = request.name
        )

        chatRoomRepository.save(room)

        joinChatRoom(user, room)

        return room
    }

    fun joinChatRoom(user: UserEntity, room: ChatRoomEntity) {
        val chat = ChatEntity(
            user = user,
            room = room
        )

        chatRepository.save(chat)
    }

    fun leaveChatRoom(user: UserEntity, room: ChatRoomEntity) {
        chatRepository.deleteByUserAndRoom(user, room)
    }

    fun deleteChatRoom(room: ChatRoomEntity) {
        chatRepository.deleteByRoom(room)
        chatRoomRepository.delete(room)
    }
}
