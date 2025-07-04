package com.playground.chat.chat.controller

import com.playground.chat.chat.data.request.CreateChatRoomRequest
import com.playground.chat.chat.data.request.FindChatMessagesRequest
import com.playground.chat.chat.data.request.FindChatRoomsRequest
import com.playground.chat.chat.data.response.ChatMessageDto
import com.playground.chat.chat.data.response.ChatRoomDto
import com.playground.chat.chat.data.response.ChatUserDto
import com.playground.chat.chat.data.response.MyChatRoomDto
import com.playground.chat.chat.service.ChatService
import com.playground.chat.global.data.Page
import com.playground.chat.global.data.Response
import com.playground.chat.global.security.AuthenticatedPrincipal
import com.playground.chat.global.security.CustomPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/chat/rooms")
class ChatController(
    private val chatService: ChatService
) {
    /**
     * 전체 채팅방 목록 조회 API
     */
    @GetMapping
    fun findChatRooms(
        @AuthenticatedPrincipal
        principal: CustomPrincipal,
        request: FindChatRoomsRequest
    ): ResponseEntity<Response<Page<List<ChatRoomDto>>>> {
        val rooms = chatService.findChatRooms(principal, request)
        val response = Response.success(rooms)

        return ResponseEntity(response, HttpStatus.OK)
    }

    /**
     * 나의 채팅방 목록 조회 API
     */
    @GetMapping("/me")
    fun findMyChatRooms(
        @AuthenticatedPrincipal
        principal: CustomPrincipal
    ): ResponseEntity<Response<List<MyChatRoomDto>>> {
        val rooms = chatService.findMyChatRooms(principal)
        val response = Response.success(rooms)

        return ResponseEntity(response, HttpStatus.OK)
    }

    /**
     * 채팅방 개설 API
     */
    @PostMapping
    fun createChatRoom(
        @AuthenticatedPrincipal
        principal: CustomPrincipal,
        @RequestBody
        request: CreateChatRoomRequest
    ): ResponseEntity<Response<ChatRoomDto>> {
        val room = chatService.createChatRoom(principal, request)
        val response = Response.success(room)

        return ResponseEntity(response, HttpStatus.OK)
    }

    /**
     * 채팅방 참여 API
     */
    @PostMapping("/{roomId}/join")
    fun joinChatRoom(
        @AuthenticatedPrincipal
        principal: CustomPrincipal,
        @PathVariable
        roomId: UUID
    ): ResponseEntity<Unit> {
        chatService.joinChatRoom(principal, roomId)

        return ResponseEntity(HttpStatus.OK)
    }

    /**
     * 채팅방 나가기 API
     */
    @PostMapping("/{roomId}/leave")
    fun leaveChatRoom(
        @AuthenticatedPrincipal
        principal: CustomPrincipal,
        @PathVariable
        roomId: UUID
    ): ResponseEntity<Unit> {
        chatService.leaveChatRoom(principal, roomId)

        return ResponseEntity(HttpStatus.OK)
    }

    /**
     * 채팅방 삭제 API
     */
    @DeleteMapping("/{roomId}")
    fun deleteChatRoom(
        @AuthenticatedPrincipal
        principal: CustomPrincipal,
        @PathVariable
        roomId: UUID
    ): ResponseEntity<Unit> {
        chatService.deleteChatRoom(principal, roomId)

        return ResponseEntity(HttpStatus.OK)
    }

    /**
     * 채팅 메세지 목록 조회 API
     */
    @GetMapping("/{roomId}/messages")
    fun findChatMessages(
        @AuthenticatedPrincipal
        principal: CustomPrincipal,
        @PathVariable
        roomId: UUID,
        request: FindChatMessagesRequest
    ): ResponseEntity<Response<Page<List<ChatMessageDto>>>> {
        val messages = chatService.findChatMessages(principal, roomId, request)
        val response = Response.success(messages)

        return ResponseEntity(response, HttpStatus.OK)
    }

    /**
     * 채팅방 유저 목록 조회 API
     */
    @GetMapping("/{roomId}/users")
    fun findChatUsers(
        @AuthenticatedPrincipal
        principal: CustomPrincipal,
        @PathVariable
        roomId: UUID
    ): ResponseEntity<Response<List<ChatUserDto>>> {
        val messages = chatService.findChatUsers(principal, roomId)
        val response = Response.success(messages)

        return ResponseEntity(response, HttpStatus.OK)
    }
}
