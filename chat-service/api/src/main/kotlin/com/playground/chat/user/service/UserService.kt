package com.playground.chat.user.service

import com.playground.chat.user.data.UserTokenDto
import com.playground.chat.global.util.PasswordUtil
import com.playground.chat.global.auth.TokenProvider
import com.playground.chat.global.auth.TokenType
import com.playground.chat.user.data.LoginUserRequest
import com.playground.chat.user.data.RegisterUserRequest
import com.playground.chat.user.data.UserDto
import com.playground.chat.user.entity.UserEntity
import com.playground.chat.user.repository.UserRepository
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class UserService(
    private val userRepository: UserRepository,
    private val tokenProvider: TokenProvider
) {
    fun register(registerUserRequest: RegisterUserRequest): UserTokenDto {
        val user = UserEntity(
            email = registerUserRequest.email,
            password = PasswordUtil.encode(registerUserRequest.password),
            nickname = registerUserRequest.nickname
        )

        userRepository.save(user)

        val userId = user.id!!

        val accessToken = tokenProvider.generate(TokenType.ACESS, userId)
        val refreshToken = tokenProvider.generate(TokenType.REFRESH, userId)

        return UserTokenDto(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    fun login(loginUserRequest: LoginUserRequest): UserTokenDto {
        val user = userRepository.findByEmail(loginUserRequest.email)
            ?: throw Exception("User Not Found : ${loginUserRequest.email}")

        if (!PasswordUtil.match(loginUserRequest.password, user.password)) {
            throw Exception("User Wrong Password : ${loginUserRequest.email}")
        }

        val userId = user.id!!

        val accessToken = tokenProvider.generate(TokenType.ACESS, userId)
        val refreshToken = tokenProvider.generate(TokenType.REFRESH, userId)

        return UserTokenDto(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    fun findMe(principal: Principal): UserDto {
        val userId = principal.name.toLong()
        val user = userRepository.findById(userId)
            .orElseThrow { throw Exception("User Not Found : ${userId}") }

        return UserDto(
            id = user.id!!,
            nickname = user.nickname
        )
    }
}
