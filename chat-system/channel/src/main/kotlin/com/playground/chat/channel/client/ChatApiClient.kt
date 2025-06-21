package com.playground.chat.channel.client

import com.playground.chat.chat.data.response.ChatRoomDto
import com.playground.chat.global.data.Response
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name = "chatApiClient", url = "\${servers.api.url}")
fun interface ChatApiClient {
    @GetMapping("/chat/rooms/me")
    fun findMyChatRooms(
        @RequestHeader("Authorization")
        token: String
    ): Response.Success<List<ChatRoomDto>>
}
