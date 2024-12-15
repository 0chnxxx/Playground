package com.playground.board.core.service

import com.playground.board.core.dto.`in`.RegisterUserCommand
import com.playground.board.global.dto.ResponseDto
import com.playground.board.web.dto.RegisterUserRequest
import com.playground.board.core.dto.out.UserDto

interface UserUseCase {
    fun registerUser(command: RegisterUserCommand): ResponseDto<UserDto>
    fun loginUser(id: String, password: String): ResponseDto<UserDto>
    fun findUser(userId: String): ResponseDto<UserDto>
    fun updatePassword(id: String, beforePassword: String, afterPassword: String)
    fun deleteUser(id: String, password: String)
}
