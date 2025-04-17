package com.playground.chat.chat.service

import com.playground.chat.chat.data.event.ChatRoomEvent
import com.playground.chat.chat.data.request.CreateChatRoomRequest
import com.playground.chat.chat.data.request.FindChatMessagesRequest
import com.playground.chat.chat.data.request.FindChatRoomsRequest
import com.playground.chat.chat.data.response.ChatMessageDto
import com.playground.chat.chat.data.response.ChatRoomDto
import com.playground.chat.global.data.Page
import com.playground.chat.user.service.UserFinder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.Principal
import java.time.ZoneOffset

@Service
@Transactional
class ChatService(
    private val userFinder: UserFinder,
    private val chatFinder: ChatFinder,
    private val chatOperator: ChatOperator,
    private val chatEventPublisher: ChatEventPublisher
) {
    fun findChatRooms(principal: Principal, request: FindChatRoomsRequest): Page<List<ChatRoomDto>> {
        val user = userFinder.findUser(principal.name.toLong())
        val rooms = chatFinder.findChatRooms(request)

        return Page(
            totalPages = rooms.totalPages,
            totalElements = rooms.totalElements,
            isFirst = rooms.isFirst,
            isLast = rooms.isLast,
            data = rooms.data.map {
                ChatRoomDto(
                    id = it.id!!,
                    name = it.name,
                    isJoined = it.users.contains(user)
                )
            }.toList()
        )
    }

    fun findMyChatRooms(principal: Principal): List<ChatRoomDto> {
        val user = userFinder.findUser(principal.name.toLong())
        val rooms = chatFinder.findMyChatRooms(user)

        return rooms.map {
            ChatRoomDto(
                id = it.id!!,
                name = it.name,
                isJoined = true
            )
        }.toList()
    }

    fun createChatRoom(principal: Principal, request: CreateChatRoomRequest): ChatRoomDto {
        val user = userFinder.findUser(principal.name.toLong())

        val room = chatOperator.createChatRoom(user, request)

        chatEventPublisher.publish(
            roomId = room.id!!.toString(),
            event = ChatRoomEvent(
                type = ChatRoomEvent.EventType.CREATE,
                roomId = room.id!!,
                userId = user.id!!,
                roomName = room.name
            )
        )

        return ChatRoomDto(
            id = room.id!!,
            name = room.name,
            isJoined = true
        )
    }

    fun joinChatRoom(principal: Principal, roomId: Long) {
        val user = userFinder.findUser(principal.name.toLong())
        val room = chatFinder.findChatRoom(roomId)

        chatOperator.joinChatRoom(user, room)

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
        val room = chatFinder.findChatRoom(roomId)

        chatOperator.leaveChatRoom(user, room)

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
        val room = chatFinder.findChatRoom(roomId)

        if (!user.isOwner(room)) {
            throw Exception("This User is Not Owner")
        }

        chatOperator.deleteChatRoom(room)

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

    fun findChatMessages(principal: Principal, roomId: Long, request: FindChatMessagesRequest): Page<List<ChatMessageDto>> {
        val user = userFinder.findUser(principal.name.toLong())
        val room = chatFinder.findChatRoom(roomId)

        val messages = chatFinder.findChatMessagesByRoom(room, request)

        return Page(
            totalPages = messages.totalPages,
            totalElements = messages.totalElements,
            isFirst = messages.isFirst,
            isLast = messages.isLast,
            data = messages.data.map {
                ChatMessageDto(
                    roomId = it.id!!.toString(),
                    sender = ChatMessageDto.Sender(
                        userId = it.sender.id!!.toString(),
                        name = it.sender.nickname
                    ),
                    content = it.content,
                    isMine = it.sender.id!! == user.id!!,
                    timestamp = it.createdAt.toEpochSecond(ZoneOffset.UTC)
                )
            }.toList()
        )
    }
}
