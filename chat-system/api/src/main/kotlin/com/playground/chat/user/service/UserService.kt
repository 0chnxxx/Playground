package com.playground.chat.user.service

import com.playground.chat.global.auth.CustomPrincipal
import com.playground.chat.global.auth.TokenProvider
import com.playground.chat.global.auth.TokenType
import com.playground.chat.global.util.PasswordUtil
import com.playground.chat.user.data.request.LoginUserRequest
import com.playground.chat.user.data.request.RegisterUserRequest
import com.playground.chat.user.data.response.UserDto
import com.playground.chat.user.data.response.UserTokenDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val tokenProvider: TokenProvider,
    private val userFinder: UserFinder,
    private val userOperator: UserOperator
) {
    @Transactional
    fun register(request: RegisterUserRequest): UserTokenDto {
        val user = userOperator.createUser(request)

        val accessToken = tokenProvider.generate(TokenType.ACESS, user.id!!)
        val refreshToken = tokenProvider.generate(TokenType.REFRESH, user.id!!)

        return UserTokenDto(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    fun refresh(principal: CustomPrincipal): UserTokenDto {
        val user = userFinder.findUser(principal.id)

        val accessToken = tokenProvider.generate(TokenType.ACESS, user.id!!)
        val refreshToken = tokenProvider.generate(TokenType.REFRESH, user.id!!)

        return UserTokenDto(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    @Transactional(readOnly = true)
    fun findMe(principal: CustomPrincipal): UserDto {
        val user = userFinder.findUser(principal.id)

        return UserDto(
            id = user.id!!,
            image = user.image,
            nickname = user.nickname
        )
    }
}
