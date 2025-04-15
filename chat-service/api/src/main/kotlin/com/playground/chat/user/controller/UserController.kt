package com.playground.chat.user.controller

import com.playground.chat.global.auth.LoginUser
import com.playground.chat.global.data.ResponseDto
import com.playground.chat.user.data.UserTokenDto
import com.playground.chat.user.data.LoginUserRequest
import com.playground.chat.user.data.RegisterUserRequest
import com.playground.chat.user.data.UserDto
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
    ): ResponseEntity<ResponseDto<UserTokenDto>> {
        val token = userService.register(registerUserRequest)
        val response = ResponseDto.of(token)

        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping("/users/login")
    fun login(
        @RequestBody
        loginUserRequest: LoginUserRequest
    ): ResponseEntity<ResponseDto<UserTokenDto>> {
        val token = userService.login(loginUserRequest)
        val response = ResponseDto.of(token)

        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/users/me")
    fun findMe(
        @LoginUser
        principal: Principal
    ): ResponseEntity<ResponseDto<UserDto>> {
        val user = userService.findMe(principal)
        val response = ResponseDto.of(user)

        return ResponseEntity(response, HttpStatus.OK)
    }
}
