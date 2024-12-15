package com.playground.board.core.service

import com.playground.board.core.dto.`in`.RegisterUserCommand
import com.playground.board.core.dto.out.UserDto
import com.playground.board.global.dto.ResponseDto
import com.playground.board.global.exception.CustomException
import com.playground.board.global.exception.ErrorCode
import com.playground.board.persistence.entity.UserEntity
import com.playground.board.persistence.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
): UserUseCase {
    override fun registerUser(command: RegisterUserCommand): ResponseDto<UserDto> {
        val isDuplicated = userRepository.findByEmail(command.email) != null

        if (isDuplicated) {
            throw CustomException(ErrorCode.DUPLICATED_EMAIL)
        }

        val user = UserEntity(
            email = command.email,
            password = command.password
        )

        userRepository.save(user)

        val userDto = UserDto(
            user.id!!,
            user.email
        )

        return ResponseDto(userDto)
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
