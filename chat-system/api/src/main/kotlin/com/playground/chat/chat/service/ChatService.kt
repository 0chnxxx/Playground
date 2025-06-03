package com.playground.chat.chat.service

import com.playground.chat.chat.data.event.ChatRoomEvent
import com.playground.chat.chat.data.request.CreateChatRoomRequest
import com.playground.chat.chat.data.request.FindChatMessagesRequest
import com.playground.chat.chat.data.request.FindChatRoomsRequest
import com.playground.chat.chat.data.response.ChatMessageDto
import com.playground.chat.chat.data.response.ChatRoomDto
import com.playground.chat.chat.data.response.ChatUserDto
import com.playground.chat.chat.data.response.MyChatRoomDto
import com.playground.chat.global.auth.UserPrincipal
import com.playground.chat.global.data.Pagination
import com.playground.chat.user.service.UserFinder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ChatService(
    private val userFinder: UserFinder,
    private val chatFinder: ChatFinder,
    private val chatOperator: ChatOperator,
    private val chatPublisher: ChatPublisher
) {
    fun findChatRooms(principal: UserPrincipal, request: FindChatRoomsRequest): Pagination<List<ChatRoomDto>> {
        val user = userFinder.findUser(principal.name.toLong())

        return chatFinder.findChatRooms(user, request)
    }

    fun findMyChatRooms(principal: UserPrincipal): List<MyChatRoomDto> {
        val user = userFinder.findUser(principal.name.toLong())

        return chatFinder.findMyChatRooms(user)
    }

    fun createChatRoom(principal: UserPrincipal, request: CreateChatRoomRequest): ChatRoomDto {
        val user = userFinder.findUser(principal.name.toLong())

        val room = chatOperator.createChatRoom(user, request)

        chatPublisher.publishChatRoomEvent(
            ChatRoomEvent(
                type = ChatRoomEvent.Type.CREATE,
                roomId = room.id!!,
                userId = principal.name.toLong(),
                roomName = room.name
            )
        )

        return room
    }

    fun joinChatRoom(principal: UserPrincipal, roomId: Long) {
        val user = userFinder.findUser(principal.name.toLong())
        val room = chatFinder.findChatRoom(roomId)

        chatOperator.joinChatRoom(user, room)

        chatPublisher.publishChatRoomEvent(
            ChatRoomEvent(
                type = ChatRoomEvent.Type.JOIN,
                roomId = room.id!!,
                userId = user.id!!,
                roomName = room.name
            )
        )
    }

    fun leaveChatRoom(principal: UserPrincipal, roomId: Long) {
        val user = userFinder.findUser(principal.name.toLong())
        val room = chatFinder.findChatRoom(roomId)

        chatOperator.leaveChatRoom(user, room)

        chatPublisher.publishChatRoomEvent(
            ChatRoomEvent(
                type = ChatRoomEvent.Type.LEAVE,
                roomId = room.id!!,
                userId = user.id!!,
                roomName = room.name
            )
        )
    }

    fun deleteChatRoom(principal: UserPrincipal, roomId: Long) {
        val user = userFinder.findUser(principal.name.toLong())
        val room = chatFinder.findChatRoom(roomId)

        if (!user.isOwner(room)) {
            throw Exception("This User is Not Owner")
        }

        chatOperator.deleteChatRoom(room)

        chatPublisher.publishChatRoomEvent(
            ChatRoomEvent(
                type = ChatRoomEvent.Type.DELETE,
                userId = user.id!!,
                roomId = room.id!!,
                roomName = room.name
            )
        )
    }

    fun findChatMessages(principal: UserPrincipal, roomId: Long, request: FindChatMessagesRequest): Pagination<List<ChatMessageDto>> {
        val user = userFinder.findUser(principal.name.toLong())
        val room = chatFinder.findChatRoom(roomId)

        return chatFinder.findChatMessagesByRoom(user, room, request)
    }

    fun findChatUsers(principal: UserPrincipal, roomId: Long): List<ChatUserDto> {
        val user = userFinder.findUser(principal.name.toLong())
        val room = chatFinder.findChatRoom(roomId)

        return chatFinder.findChatUsers(user, room)
    }
}
