package com.playground.chat.user.service

import com.playground.chat.global.security.CustomPrincipal
import com.playground.chat.global.token.TokenProvider
import com.playground.chat.global.token.TokenType
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
    private val userOperator: UserOperator,
    private val userValidator: UserValidator
) {
    @Transactional
    fun register(request: RegisterUserRequest): UserTokenDto {
        userValidator.checkDuplicatedUser(request.email)

        val user = userOperator.createUser(request.toUser())

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

        userValidator.validPassword(request.password, user.password)

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
