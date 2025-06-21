package com.playground.chat.chat.service

import com.playground.chat.chat.data.event.ChatRoomEvent
import com.playground.chat.chat.data.request.CreateChatRoomRequest
import com.playground.chat.chat.data.request.FindChatMessagesRequest
import com.playground.chat.chat.data.request.FindChatRoomsRequest
import com.playground.chat.chat.data.response.ChatMessageDto
import com.playground.chat.chat.data.response.ChatRoomDto
import com.playground.chat.chat.data.response.ChatUserDto
import com.playground.chat.chat.data.response.MyChatRoomDto
import com.playground.chat.global.data.Page
import com.playground.chat.global.security.CustomPrincipal
import com.playground.chat.user.service.UserFinder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ChatService(
    private val userFinder: UserFinder,
    private val chatFinder: ChatFinder,
    private val chatOperator: ChatOperator,
    private val chatValidator: ChatValidator,
    private val chatPublisher: ChatPublisher
) {
    @Transactional(readOnly = true)
    fun findChatRooms(principal: CustomPrincipal, request: FindChatRoomsRequest): Page<List<ChatRoomDto>> {
        val user = userFinder.findUser(principal.id)

        return chatFinder.findChatRooms(user.id!!, request)
    }

    @Transactional(readOnly = true)
    fun findMyChatRooms(principal: CustomPrincipal): List<MyChatRoomDto> {
        val user = userFinder.findUser(principal.id)

        return chatFinder.findMyChatRooms(user.id!!)
    }

    @Transactional
    fun createChatRoom(principal: CustomPrincipal, request: CreateChatRoomRequest): ChatRoomDto {
        val user = userFinder.findUser(principal.id)
        val room = chatOperator.createChatRoom(user, request)

        chatPublisher.publishChatRoomEvent(
            ChatRoomEvent(
                type = ChatRoomEvent.Type.CREATE,
                roomId = room.id,
                userId = principal.id,
                nickname = user.nickname,
                roomName = room.name
            )
        )

        return room
    }

    @Transactional
    fun joinChatRoom(principal: CustomPrincipal, roomId: UUID) {
        val user = userFinder.findUser(principal.id)
        val room = chatFinder.findChatRoom(roomId)

        chatOperator.joinChatRoom(user, room)

        chatPublisher.publishChatRoomEvent(
            ChatRoomEvent(
                type = ChatRoomEvent.Type.JOIN,
                roomId = room.id!!,
                userId = user.id!!,
                nickname = user.nickname,
                roomName = room.name
            )
        )
    }

    @Transactional
    fun leaveChatRoom(principal: CustomPrincipal, roomId: UUID) {
        val user = userFinder.findUser(principal.id)
        val room = chatFinder.findChatRoom(roomId)

        chatOperator.leaveChatRoom(user, room)

        chatPublisher.publishChatRoomEvent(
            ChatRoomEvent(
                type = ChatRoomEvent.Type.LEAVE,
                roomId = room.id!!,
                userId = user.id!!,
                nickname = user.nickname,
                roomName = room.name
            )
        )
    }

    @Transactional
    fun deleteChatRoom(principal: CustomPrincipal, roomId: UUID) {
        val user = userFinder.findUser(principal.id)
        val room = chatFinder.findChatRoom(roomId)

        chatValidator.checkRoomOwner(room, user)
        chatOperator.deleteChatRoom(room)

        chatPublisher.publishChatRoomEvent(
            ChatRoomEvent(
                type = ChatRoomEvent.Type.DELETE,
                userId = user.id!!,
                roomId = room.id!!,
                nickname = user.nickname,
                roomName = room.name
            )
        )
    }

    @Transactional(readOnly = true)
    fun findChatMessages(
        principal: CustomPrincipal,
        roomId: UUID,
        request: FindChatMessagesRequest
    ): Page<List<ChatMessageDto>> {
        val user = userFinder.findUser(principal.id)
        val room = chatFinder.findChatRoom(roomId)

        return chatFinder.findChatMessagesByRoom(user.id!!, room.id!!, request)
    }

    @Transactional(readOnly = true)
    fun findChatUsers(principal: CustomPrincipal, roomId: UUID): List<ChatUserDto> {
        val user = userFinder.findUser(principal.id)
        val room = chatFinder.findChatRoom(roomId)

        return chatFinder.findChatUsers(user.id!!, room.id!!)
    }
}
