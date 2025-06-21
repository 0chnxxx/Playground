package com.playground.chat.user.service

import com.playground.chat.global.util.PasswordUtil
import com.playground.chat.user.domain.User
import com.playground.chat.user.entity.UserEntity
import com.playground.chat.user.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class UserOperator(
    private val userRepository: UserRepository
) {
    fun createUser(user: User): User {
        val entity = UserEntity(
            email = user.email,
            password = PasswordUtil.encode(user.password),
            nickname = user.nickname,
            role = user.role
        )

        userRepository.saveUser(entity)

        user.id = entity.id

        return user
    }
}
