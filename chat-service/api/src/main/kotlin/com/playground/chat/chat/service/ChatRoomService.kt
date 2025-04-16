package com.playground.chat.chat.service

import com.playground.chat.chat.data.event.ChatRoomEvent
import com.playground.chat.chat.data.request.CreateChatRoomRequest
import com.playground.chat.chat.data.request.FindChatRoomsRequest
import com.playground.chat.chat.data.response.RoomDto
import com.playground.chat.global.data.Page
import com.playground.chat.user.service.UserFinder
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.Principal

@Service
@Transactional
class ChatRoomService(
    private val userFinder: UserFinder,
    private val chatRoomFinder: ChatRoomFinder,
    private val chatRoomOperator: ChatRoomOperator,
    private val chatEventPublisher: ChatEventPublisher
) {
    fun findChatRooms(request: FindChatRoomsRequest): Page<List<RoomDto>> {
        val rooms = chatRoomFinder.findChatRooms(request)

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

        return user.rooms.map {
            RoomDto(
                id = it.id!!,
                name = it.name
            )
        }.toList()
    }

    fun createChatRoom(principal: Principal, request: CreateChatRoomRequest): RoomDto {
        val user = userFinder.findUser(principal.name.toLong())

        val room = chatRoomOperator.createChatRoom(user, request)

        chatEventPublisher.publish(
            roomId = room.id!!.toString(),
            event = ChatRoomEvent(
                type = ChatRoomEvent.EventType.CREATE,
                roomId = room.id!!,
                userId = user.id!!,
                roomName = room.name
            )
        )

        return RoomDto(
            id = room.id!!,
            name = room.name
        )
    }

    fun joinChatRoom(principal: Principal, roomId: Long) {
        val user = userFinder.findUser(principal.name.toLong())
        val room = chatRoomFinder.findChatRoom(roomId)

        chatRoomOperator.joinChatRoom(user, room)

        chatEventPublisher.publish(
            roomId = room.id!!.toString(),
            event = ChatRoomEvent(
                type = ChatRoomEvent.EventType.JOIN,
                roomId = room.id!!,
                userId = user.id!!,
                roomName = room.name
            )
        )
    }

    fun leaveChatRoom(principal: Principal, roomId: Long) {
        val user = userFinder.findUser(principal.name.toLong())
        val room = chatRoomFinder.findChatRoom(roomId)

        chatRoomOperator.leaveChatRoom(user, room)

        chatEventPublisher.publish(
            roomId = room.id!!.toString(),
            event = ChatRoomEvent(
                type = ChatRoomEvent.EventType.LEAVE,
                roomId = room.id!!,
                userId = user.id!!,
                roomName = room.name
            )
        )
    }

    fun deleteChatRoom(principal: Principal, roomId: Long) {
        val user = userFinder.findUser(principal.name.toLong())
        val room = chatRoomFinder.findChatRoom(roomId)

        if(!user.isOwner(room)) {
            throw Exception("This User is Not Owner")
        }

        chatRoomOperator.deleteChatRoom(room)

        chatEventPublisher.publish(
            roomId = room.id!!.toString(),
            event = ChatRoomEvent(
                type = ChatRoomEvent.EventType.DELETE,
                userId = user.id!!,
                roomId = room.id!!,
                roomName = room.name
            )
        )
    }
}
