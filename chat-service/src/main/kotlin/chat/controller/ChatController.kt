package com.playground.chat.chat.controller

import com.playground.chat.chat.data.CreateChatRoomRequest
import com.playground.chat.chat.data.FindChatRoomsRequest
import com.playground.chat.chat.data.RoomDto
import com.playground.chat.chat.service.ChatService
import com.playground.chat.global.auth.Principal
import com.playground.chat.global.auth.UserPrincipal
import com.playground.chat.global.dto.ResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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
        @RequestBody
        request: FindChatRoomsRequest
    ): ResponseEntity<ResponseDto<List<RoomDto>>> {
        val rooms = chatService.findChatRooms(request)
        val response = ResponseDto.of(rooms)

        return ResponseEntity(response, HttpStatus.OK)
    }

    /**
     * 나의 채팅방 목록 조회 API
     */
    @GetMapping("/me")
    fun findMyChatRooms(
        @Principal
        principal: UserPrincipal
    ): ResponseEntity<ResponseDto<List<RoomDto>>> {
        val rooms = chatService.findMyChatRooms(principal)
        val response = ResponseDto.of(rooms)

        return ResponseEntity(response, HttpStatus.OK)
    }

    /**
     * 채팅방 개설 API
     */
    @PostMapping
    fun createChatRoom(
        @Principal
        principal: UserPrincipal,
        @RequestBody
        request: CreateChatRoomRequest
    ): ResponseEntity<Void> {
        chatService.createChatRoom(principal, request)

        return ResponseEntity(HttpStatus.OK)
    }

    /**
     * 채팅방 참여 API
     */
    @PostMapping("/{roomId}/join")
    fun joinChatRoom(
        @Principal
        principal: UserPrincipal,
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
        @Principal
        principal: UserPrincipal,
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
        @Principal
        principal: UserPrincipal,
        @PathVariable
        roomId: Long
    ): ResponseEntity<Void> {
        chatService.deleteChatRoom(principal, roomId)

        return ResponseEntity(HttpStatus.OK)
    }
}
