package com.playground.chat.user.service

import com.playground.chat.user.domain.User
import com.playground.chat.user.repository.UserRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserFinder(
    private val userRepository: UserRepository
) {
    fun findUser(userId: UUID): User {
        return userRepository.findUserById(userId)?.toUser()
            ?: throw Exception("User Not Found")
    }

    fun findUser(email: String): User {
        return userRepository.findUserByEmail(email)?.toUser()
            ?: throw Exception("User Not Found")
    }

    fun existsUser(email: String): Boolean {
        return userRepository.findUserByEmail(email) != null
    }
}
