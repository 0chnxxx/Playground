package com.playground.chat.user.controller

import com.playground.chat.global.data.TokenDto
import com.playground.chat.global.dto.ResponseDto
import com.playground.chat.user.data.UserDto
import com.playground.chat.user.dto.LoginUserRequest
import com.playground.chat.user.dto.RegisterUserRequest
import com.playground.chat.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
    private val userService: UserService
) {
    @PostMapping("/users/register")
    fun register(
        @RequestBody
        registerUserRequest: RegisterUserRequest
    ): ResponseEntity<ResponseDto<TokenDto>> {
        val token = userService.register(registerUserRequest)
        val response = ResponseDto.of(token)

        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping("/users/login")
    fun login(
        @RequestBody
        loginUserRequest: LoginUserRequest
    ): ResponseEntity<ResponseDto<TokenDto>> {
        val token = userService.login(loginUserRequest)
        val response = ResponseDto.of(token)

        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/users/me")
    fun findMe(
        @RequestHeader("Authorization")
        authorization: String?
    ): ResponseEntity<ResponseDto<UserDto>> {
        val user = userService.findMe(authorization)
        val response = ResponseDto.of(user)

        return ResponseEntity(response, HttpStatus.OK)
    }
}
