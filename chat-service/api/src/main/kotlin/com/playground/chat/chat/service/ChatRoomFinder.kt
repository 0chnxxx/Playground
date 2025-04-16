package com.playground.chat.chat.service

import com.playground.chat.chat.data.request.FindChatRoomsRequest
import com.playground.chat.chat.entity.ChatRoomEntity
import com.playground.chat.chat.repository.ChatRoomRepository
import com.playground.chat.global.data.Page
import com.playground.chat.user.entity.UserEntity
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component

@Component
class ChatRoomFinder(
    private val chatRoomRepository: ChatRoomRepository
) {
    fun findChatRooms(request: FindChatRoomsRequest): Page<List<ChatRoomEntity>> {
        val page = (request.page - 1).coerceAtLeast(0)
        val size = request.size
        val pageable = PageRequest.of(page, size)
        val chatRooms = chatRoomRepository.findAll(pageable)

        return Page(
            totalPages = chatRooms.totalPages,
            totalElements = chatRooms.totalElements,
            isFirst = chatRooms.isFirst,
            isLast = chatRooms.isLast,
            data = chatRooms.content
        )
    }

    fun findChatRoom(roomId: Long): ChatRoomEntity {
        return chatRoomRepository.findById(roomId)
            .orElseThrow { Exception("Chat Room Not Found") }
    }
}
