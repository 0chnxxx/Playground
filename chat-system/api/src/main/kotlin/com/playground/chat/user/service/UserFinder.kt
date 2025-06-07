package com.playground.chat.user.service

import com.playground.chat.user.entity.UserEntity
import com.playground.chat.user.repository.UserRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserFinder(
    private val userRepository: UserRepository
) {
    fun findUser(userId: UUID): UserEntity {
        return userRepository.findUserById(userId)
            ?: throw Exception("User Not Found")
    }

    fun findUser(email: String): UserEntity {
        return userRepository.findUserByEmail(email)
            ?: throw Exception("User Not Found")
    }

    fun existsUser(email: String): Boolean {
        return userRepository.findUserByEmail(email) != null
    }
}
