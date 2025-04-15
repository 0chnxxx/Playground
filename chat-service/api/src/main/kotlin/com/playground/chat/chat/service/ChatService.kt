package com.playground.chat.chat.service

import com.playground.chat.chat.data.CreateChatRoomRequest
import com.playground.chat.chat.data.FindChatRoomsRequest
import com.playground.chat.chat.data.RoomDto
import com.playground.chat.global.data.Page
import com.playground.chat.user.service.UserFinder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.Principal

@Service
@Transactional
class ChatService(
    private val userFinder: UserFinder,
    private val chatFinder: ChatFinder,
    private val chatOperator: ChatOperator,
) {
    fun findChatRooms(request: FindChatRoomsRequest): Page<List<RoomDto>> {
        val rooms = chatFinder.findChatRooms(request)

        return Page(
            totalPages = rooms.totalPages,
            totalElements = rooms.totalElements,
            isFirst = rooms.isFirst,
            isLast = rooms.isLast,
            data = rooms.data.map {
                RoomDto(
                    id = it.id!!,
                    name = it.name
                )
            }.toList()
        )
    }

    fun findMyChatRooms(principal: Principal): List<RoomDto> {
        val user = userFinder.findUser(principal.name.toLong())
        val rooms = chatFinder.findChatRoomsByUser(user)

        return rooms.map {
            RoomDto(
                id = it.id!!,
                name = it.name
            )
        }.toList()
    }

    fun createChatRoom(principal: Principal, request: CreateChatRoomRequest): RoomDto {
        val user = userFinder.findUser(principal.name.toLong())

        val room = chatOperator.createChatRoom(user, request)

        return RoomDto(
            id = room.id!!,
            name = room.name
        )
    }

    fun joinChatRoom(principal: Principal, roomId: Long) {
        val user = userFinder.findUser(principal.name.toLong())
        val room = chatFinder.findChatRoom(roomId)

        chatOperator.joinChatRoom(user, room)
    }

    fun leaveChatRoom(principal: Principal, roomId: Long) {
        val user = userFinder.findUser(principal.name.toLong())
        val room = chatFinder.findChatRoom(roomId)

        chatOperator.leaveChatRoom(user, room)
    }

    fun deleteChatRoom(principal: Principal, roomId: Long) {
        val user = userFinder.findUser(principal.name.toLong())
        val room = chatFinder.findChatRoom(roomId)

        if(!user.isOwner(room)) {
            throw Exception("This User is Not Owner")
        }

        chatOperator.deleteChatRoom(room)
    }
}
