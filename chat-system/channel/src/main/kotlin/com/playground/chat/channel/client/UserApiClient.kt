package com.playground.chat.channel.client

import com.playground.chat.global.data.Response
import com.playground.chat.user.data.response.UserDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name = "userApiClient", url = "\${servers.api.url}")
fun interface UserApiClient {
    @GetMapping("/users/me")
    fun findMe(
        @RequestHeader("Authorization")
        token: String
    ): Response.Success<UserDto>
}
