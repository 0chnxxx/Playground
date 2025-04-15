package com.playground.chat.user.service

import com.playground.chat.user.data.UserTokenDto
import com.playground.chat.global.util.PasswordUtil
import com.playground.chat.global.auth.TokenProvider
import com.playground.chat.global.auth.TokenType
import com.playground.chat.user.data.LoginUserRequest
import com.playground.chat.user.data.RegisterUserRequest
import com.playground.chat.user.data.UserDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.Principal

@Service
@Transactional
class UserService(
    private val tokenProvider: TokenProvider,
    private val userFinder: UserFinder,
    private val userOperator: UserOperator
) {
    fun register(request: RegisterUserRequest): UserTokenDto {
        val user = userOperator.createUser(request)

        val accessToken = tokenProvider.generate(TokenType.ACESS, user.id!!)
        val refreshToken = tokenProvider.generate(TokenType.REFRESH, user.id!!)

        return UserTokenDto(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    fun login(request: LoginUserRequest): UserTokenDto {
        val user = userFinder.findUser(request.email)

        if (!PasswordUtil.match(request.password, user.password)) {
            throw Exception("User Wrong Password")
        }

        val accessToken = tokenProvider.generate(TokenType.ACESS, user.id!!)
        val refreshToken = tokenProvider.generate(TokenType.REFRESH, user.id!!)

        return UserTokenDto(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    fun findMe(principal: Principal): UserDto {
        val user = userFinder.findUser(principal.name.toLong())

        return UserDto(
            id = user.id!!,
            nickname = user.nickname
        )
    }
}
