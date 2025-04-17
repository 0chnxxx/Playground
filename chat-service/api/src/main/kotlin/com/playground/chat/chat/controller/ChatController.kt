package com.playground.chat.chat.controller

import com.playground.chat.chat.data.request.CreateChatRoomRequest
import com.playground.chat.chat.data.request.FindChatMessagesRequest
import com.playground.chat.chat.data.request.FindChatRoomsRequest
import com.playground.chat.chat.data.response.ChatMessageDto
import com.playground.chat.chat.data.response.ChatRoomDto
import com.playground.chat.chat.service.ChatService
import com.playground.chat.global.auth.LoginUser
import com.playground.chat.global.data.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

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
        @LoginUser
        principal: Principal,
        request: FindChatRoomsRequest
    ): ResponseEntity<Response<List<ChatRoomDto>>> {
        val rooms = chatService.findChatRooms(principal, request)
        val response = Response.of(rooms)

        return ResponseEntity(response, HttpStatus.OK)
    }

    /**
     * 나의 채팅방 목록 조회 API
     */
    @GetMapping("/me")
    fun findMyChatRooms(
        @LoginUser
        principal: Principal
    ): ResponseEntity<Response<List<ChatRoomDto>>> {
        val rooms = chatService.findMyChatRooms(principal)
        val response = Response.of(rooms)

        return ResponseEntity(response, HttpStatus.OK)
    }

    /**
     * 채팅방 개설 API
     */
    @PostMapping
    fun createChatRoom(
        @LoginUser
        principal: Principal,
        @RequestBody
        request: CreateChatRoomRequest
    ): ResponseEntity<Response<ChatRoomDto>> {
        val room = chatService.createChatRoom(principal, request)
        val response = Response.of(room)

        return ResponseEntity(response, HttpStatus.OK)
    }

    /**
     * 채팅방 참여 API
     */
    @PostMapping("/{roomId}/join")
    fun joinChatRoom(
        @LoginUser
        principal: Principal,
        @PathVariable
        roomId: Long
    ): ResponseEntity<Void> {
        chatService.joinChatRoom(principal, roomId)

        return ResponseEntity(HttpStatus.OK)
    }

    /**
     * 채팅방 나가기 API
     */
    @PostMapping("/{roomId}/leave")
    fun leaveChatRoom(
        @LoginUser
        principal: Principal,
        @PathVariable
        roomId: Long
    ): ResponseEntity<Void> {
        chatService.leaveChatRoom(principal, roomId)

        return ResponseEntity(HttpStatus.OK)
    }

    /**
     * 채팅방 삭제 API
     */
    @DeleteMapping("/{roomId}")
    fun deleteChatRoom(
        @LoginUser
        principal: Principal,
        @PathVariable
        roomId: Long
    ): ResponseEntity<Void> {
        chatService.deleteChatRoom(principal, roomId)

        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/{roomId}/messages")
    fun findChatMessages(
        @LoginUser
        principal: Principal,
        @PathVariable
        roomId: Long,
        request: FindChatMessagesRequest
    ): ResponseEntity<Response<List<ChatMessageDto>>> {
        val messages = chatService.findChatMessages(principal, roomId, request)
        val response = Response.of(messages)

        return ResponseEntity(response, HttpStatus.OK)
    }
}
