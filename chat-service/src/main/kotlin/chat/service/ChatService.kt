package com.playground.chat.chat.service

import com.playground.chat.chat.data.CreateChatRoomRequest
import com.playground.chat.chat.data.FindChatRoomsRequest
import com.playground.chat.chat.data.RoomDto
import com.playground.chat.global.auth.UserPrincipal
import com.playground.chat.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ChatService(
    private val userRepository: UserRepository
) {
    fun findChatRooms(request: FindChatRoomsRequest): List<RoomDto> {
        TODO("Not yet implemented")
    }

    fun findMyChatRooms(principal: UserPrincipal): List<RoomDto> {
        val user = userRepository.findById(principal.id)
            .orElseThrow { throw Exception("User Not Found : ${principal.name}") }

        val rooms = user.rooms.map {
            RoomDto(
                id = it.id!!,
                name = it.name!!
            )
        }.toList()

        return rooms
    }

    fun createChatRoom(principal: UserPrincipal, request: CreateChatRoomRequest) {
        TODO("Not yet implemented")
    }

    fun joinChatRoom(principal: UserPrincipal, roomId: Long) {
        TODO("Not yet implemented")
    }

    fun leaveChatRoom(principal: UserPrincipal, roomId: Long) {
        TODO("Not yet implemented")
    }

    fun deleteChatRoom(principal: UserPrincipal, roomId: Long) {
        TODO("Not yet implemented")
    }
}
