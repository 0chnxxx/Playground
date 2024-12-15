package com.playground.board.web.controller

import com.playground.board.core.dto.out.UserDto
import com.playground.board.core.service.UserUseCase
import com.playground.board.global.dto.ResponseDto
import com.playground.board.web.dto.RegisterUserRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
    private val userUseCase: UserUseCase
) {
    @PostMapping
    fun registerUser(
        @RequestBody request: RegisterUserRequest
    ): ResponseEntity<ResponseDto<UserDto>> {
        val command = request.toCommand()
        val response = userUseCase.registerUser(command)

        return ResponseEntity(response, HttpStatus.CREATED)
    }
}
