package com.playground.chat.user.service

import com.playground.chat.global.util.PasswordUtil
import com.playground.chat.user.data.request.RegisterUserRequest
import com.playground.chat.user.entity.UserEntity
import com.playground.chat.user.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class UserOperator(
    private val userRepository: UserRepository
) {
    fun createUser(registerUserRequest: RegisterUserRequest): UserEntity {
        val user = UserEntity(
            email = registerUserRequest.email,
            password = PasswordUtil.encode(registerUserRequest.password),
            nickname = registerUserRequest.nickname
        )

        userRepository.save(user)

        return user
    }
}
