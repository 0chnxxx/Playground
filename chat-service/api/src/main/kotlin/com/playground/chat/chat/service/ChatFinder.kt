package com.playground.chat.chat.service

import com.playground.chat.chat.data.request.FindChatMessagesRequest
import com.playground.chat.chat.data.request.FindChatRoomsRequest
import com.playground.chat.chat.data.response.ChatMessageDto
import com.playground.chat.chat.data.response.ChatRoomDto
import com.playground.chat.chat.entity.ChatRoomEntity
import com.playground.chat.chat.repository.ChatRepository
import com.playground.chat.global.data.Pagination
import com.playground.chat.user.entity.UserEntity
import org.springframework.stereotype.Component

@Component
class ChatFinder(
    private val chatRepository: ChatRepository
) {
    fun findChatRooms(user: UserEntity, request: FindChatRoomsRequest): Pagination<List<ChatRoomDto>> {
        return chatRepository.findChatRooms(user.id!!, request)
    }

    fun findChatRoom(roomId: Long): ChatRoomEntity {
        return chatRepository.findChatRoomByRoomId(roomId)
            ?: throw Exception("Chat Room Not Found")
    }

    fun findMyChatRooms(user: UserEntity): List<ChatRoomDto> {
        return chatRepository.findChatRoomsByUserId(user.id!!)
    }

    fun findChatMessagesByRoom(user: UserEntity, room: ChatRoomEntity, request: FindChatMessagesRequest): Pagination<List<ChatMessageDto>> {
        return chatRepository.findChatMessagesByRoomId(user.id!!, room.id!!, request)
    }
}
