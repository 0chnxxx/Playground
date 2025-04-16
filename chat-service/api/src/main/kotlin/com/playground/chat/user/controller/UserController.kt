package com.playground.chat.user.controller

import com.playground.chat.global.auth.LoginUser
import com.playground.chat.global.data.Response
import com.playground.chat.user.data.response.UserTokenDto
import com.playground.chat.user.data.request.LoginUserRequest
import com.playground.chat.user.data.request.RegisterUserRequest
import com.playground.chat.user.data.response.UserDto
import com.playground.chat.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
class UserController(
    private val userService: UserService
) {
    @PostMapping("/users/register")
    fun register(
        @RequestBody
        registerUserRequest: RegisterUserRequest
    ): ResponseEntity<Response<UserTokenDto>> {
        val token = userService.register(registerUserRequest)
        val response = Response.of(token)

        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping("/users/login")
    fun login(
        @RequestBody
        loginUserRequest: LoginUserRequest
    ): ResponseEntity<Response<UserTokenDto>> {
        val token = userService.login(loginUserRequest)
        val response = Response.of(token)

        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/users/me")
    fun findMe(
        @LoginUser
        principal: Principal
    ): ResponseEntity<Response<UserDto>> {
        val user = userService.findMe(principal)
        val response = Response.of(user)

        return ResponseEntity(response, HttpStatus.OK)
    }
}
