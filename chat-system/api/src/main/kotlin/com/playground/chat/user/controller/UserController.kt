package com.playground.chat.user.controller

import com.playground.chat.global.data.Response
import com.playground.chat.global.security.AuthenticatedPrincipal
import com.playground.chat.global.security.CustomPrincipal
import com.playground.chat.user.data.request.LoginUserRequest
import com.playground.chat.user.data.request.RegisterUserRequest
import com.playground.chat.user.data.response.UserDto
import com.playground.chat.user.data.response.UserTokenDto
import com.playground.chat.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService
) {
    /**
     * 유저 회원가입 API
     */
    @PostMapping("/users/register")
    fun register(
        @RequestBody
        registerUserRequest: RegisterUserRequest
    ): ResponseEntity<Response<UserTokenDto>> {
        val token = userService.register(registerUserRequest)
        val response = Response.success(token)

        return ResponseEntity(response, HttpStatus.OK)
    }

    /**
     * 유저 로그인 API
     */
    @PostMapping("/users/login")
    fun login(
        @RequestBody
        loginUserRequest: LoginUserRequest
    ): ResponseEntity<Response<UserTokenDto>> {
        val token = userService.login(loginUserRequest)
        val response = Response.success(token)

        return ResponseEntity(response, HttpStatus.OK)
    }

    /**
     * 유저 토큰 재발급 API
     */
    @PostMapping("/users/refresh")
    fun refresh(
        @AuthenticatedPrincipal
        principal: CustomPrincipal
    ): ResponseEntity<Response<UserTokenDto>> {
        val token = userService.refresh(principal)
        val response = Response.success(token)

        return ResponseEntity(response, HttpStatus.OK)
    }

    /**
     * 내 정보 조회 API
     */
    @GetMapping("/users/me")
    fun findMe(
        @AuthenticatedPrincipal
        principal: CustomPrincipal
    ): ResponseEntity<Response<UserDto>> {
        val user = userService.findMe(principal)
        val response = Response.success(user)

        return ResponseEntity(response, HttpStatus.OK)
    }
}
