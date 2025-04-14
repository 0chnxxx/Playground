package com.playground.chat.user.service

import com.playground.chat.global.auth.JwtProvider
import com.playground.chat.auth.util.PasswordUtil
import com.playground.chat.user.repository.UserRepository
import com.playground.chat.user.dto.LoginUserRequest
import com.playground.chat.user.dto.RegisterUserRequest
import com.playground.chat.global.data.TokenDto
import com.playground.chat.user.data.UserDto
import com.playground.chat.user.entity.UserEntity
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider
) {
    fun register(registerUserRequest: RegisterUserRequest): TokenDto {
        val user = UserEntity(
            email = registerUserRequest.email,
            password = PasswordUtil.encode(registerUserRequest.password),
            nickname = registerUserRequest.nickname
        )

        userRepository.save(user)

        return jwtProvider.generate(user)
    }

    fun login(loginUserRequest: LoginUserRequest): TokenDto {
        val user = userRepository.findByEmail(loginUserRequest.email)
            ?: throw Exception("User Not Found : ${loginUserRequest.email}")

        if (!PasswordUtil.match(loginUserRequest.password, user.password)) {
            throw Exception("User Wrong Password : ${loginUserRequest.email}")
        }

        return jwtProvider.generate(user)
    }

    fun findMe(authorization: String?): UserDto {
        val token = authorization
            ?.takeIf { it.startsWith("Bearer ") }
            ?.substring(7)

        if (token == null) {
            throw Exception("Token Not Found")
        }

        val userId = jwtProvider.parse(token, "userId").toString().toLong()
        val user = userRepository.findById(userId)
            .orElseThrow { throw Exception("User Not Found : ${userId}") }

        return UserDto(
            id = user.id!!,
            nickname = user.nickname
        )
    }
}
