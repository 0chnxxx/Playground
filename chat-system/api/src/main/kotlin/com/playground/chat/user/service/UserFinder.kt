package com.playground.chat.user.service

import com.playground.chat.user.data.exception.UserErrorMessage
import com.playground.chat.user.data.exception.UserException
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
            ?: throw UserException(UserErrorMessage.NOT_FOUND_USER)
    }

    fun findUser(email: String): User {
        return userRepository.findUserByEmail(email)?.toUser()
            ?: throw UserException(UserErrorMessage.NOT_FOUND_USER)
    }
}
