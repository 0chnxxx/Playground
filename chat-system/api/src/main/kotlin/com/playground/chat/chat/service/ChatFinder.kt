package com.playground.chat.chat.service

import com.playground.chat.chat.data.request.FindChatMessagesRequest
import com.playground.chat.chat.data.request.FindChatRoomsRequest
import com.playground.chat.chat.data.response.ChatMessageDto
import com.playground.chat.chat.data.response.ChatRoomDto
import com.playground.chat.chat.data.response.ChatUserDto
import com.playground.chat.chat.data.response.MyChatRoomDto
import com.playground.chat.chat.domain.Chat
import com.playground.chat.chat.domain.ChatRoom
import com.playground.chat.chat.entity.ChatMessageEntity
import com.playground.chat.chat.repository.ChatRepository
import com.playground.chat.global.data.Page
import org.springframework.stereotype.Component
import java.util.*

@Component
class ChatFinder(
    private val chatRepository: ChatRepository
) {
    fun findChat(roomId: UUID, userId: UUID): Chat {
        return chatRepository.findChat(roomId, userId)?.toChat()
            ?: throw Exception("Chat Not Found")
    }

    fun findChatRoom(roomId: UUID): ChatRoom {
        return chatRepository.findChatRoomByRoomId(roomId)?.toRoom()
            ?: throw Exception("Chat Room Not Found")
    }

    fun findMyChatRooms(userId: UUID): List<MyChatRoomDto> {
        return chatRepository.findChatRoomsByUserId(userId)
    }

    fun findChatRooms(userId: UUID, request: FindChatRoomsRequest): Page<List<ChatRoomDto>> {
        return chatRepository.findChatRooms(userId, request)
    }

    fun findLastChatMessageByRoom(roomId: UUID): ChatMessageEntity {
        return chatRepository.findLastChatMessageByRoomId(roomId)
            ?: throw Exception("Chat Message Not Found")
    }

    fun findChatMessagesByRoom(userId: UUID, roomId: UUID, request: FindChatMessagesRequest): Page<List<ChatMessageDto>> {
        return chatRepository.findChatMessagesByRoomId(userId, roomId, request)
    }

    fun findChatUsers(userId: UUID, roomId: UUID): List<ChatUserDto> {
        return chatRepository.findChatUsersByRoomId(userId, roomId)
    }
}
