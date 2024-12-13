package com.playground.board.core.service

import com.playground.board.core.dto.ResponseDto
import com.playground.board.web.dto.RegisterUserRequest
import com.playground.board.core.dto.UserDto

interface UserUseCase {
    fun registerUser(request: RegisterUserRequest): ResponseDto<UserDto>
    fun loginUser(id: String, password: String): ResponseDto<UserDto>
    fun findUser(userId: String): ResponseDto<UserDto>
    fun updatePassword(id: String, beforePassword: String, afterPassword: String)
    fun deleteUser(id: String, password: String)
}
