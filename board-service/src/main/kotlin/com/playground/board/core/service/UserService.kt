package com.playground.board.core.service

import com.playground.board.core.domain.User
import com.playground.board.core.dto.ResponseDto
import com.playground.board.core.dto.UserDto
import com.playground.board.persistence.UserJpaRepository
import com.playground.board.web.dto.RegisterUserRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserService(
    private val userJpaRepository: UserJpaRepository
): UserUseCase {
    override fun registerUser(request: RegisterUserRequest): ResponseDto<UserDto> {
        val user = User(
            email = request.email,
            password = request.password
        )

        userJpaRepository.save(user)

        val userDto = UserDto(
            user.id!!,
            user.email
        )

        return ResponseDto(
            status = HttpStatus.CREATED.value(),
            statusName = HttpStatus.CREATED.name,
            serverDataTime = LocalDateTime.now(),
            null,
            userDto
        )
    }

    override fun loginUser(id: String, password: String): ResponseDto<UserDto> {
        TODO("Not yet implemented")
    }

    override fun findUser(userId: String): ResponseDto<UserDto> {
        TODO("Not yet implemented")
    }

    override fun updatePassword(id: String, beforePassword: String, afterPassword: String) {
        TODO("Not yet implemented")
    }

    override fun deleteUser(id: String, password: String) {
        TODO("Not yet implemented")
    }
}
