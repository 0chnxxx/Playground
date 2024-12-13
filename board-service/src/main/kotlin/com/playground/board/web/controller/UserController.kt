package com.playground.board.web.controller

import com.playground.board.core.dto.ResponseDto
import com.playground.board.web.dto.RegisterUserRequest
import com.playground.board.core.dto.UserDto
import com.playground.board.core.service.UserUseCase
import lombok.extern.log4j.Log4j2
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Log4j2
@RestController
@RequestMapping("/users")
class UserController(
    private val userUseCase: UserUseCase
) {
    @PostMapping
    fun registerUser(
        @RequestBody request: RegisterUserRequest
    ): ResponseEntity<ResponseDto<UserDto>> {
        val response = userUseCase.registerUser(request)
        return ResponseEntity(response, HttpStatus.CREATED)
    }
}
