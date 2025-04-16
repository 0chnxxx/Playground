package com.playground.chat.channel.client.chat

import com.playground.chat.chat.data.RoomDto
import com.playground.chat.global.data.ResponseDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name = "chatApiClient", url = "\${servers.api.url}")
interface ChatApiClient {
    @GetMapping("/chat/rooms/me")
    fun findMyChatRooms(
        @RequestHeader("Authorization")
        token: String
    ): ResponseDto<List<RoomDto>>
}
