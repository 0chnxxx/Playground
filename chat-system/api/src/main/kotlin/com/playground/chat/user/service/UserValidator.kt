package com.playground.chat.user.service

import com.playground.chat.global.util.PasswordUtil
import com.playground.chat.user.data.exception.UserErrorMessage
import com.playground.chat.user.data.exception.UserException
import com.playground.chat.user.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class UserValidator(
    private val userRepository: UserRepository
) {
    fun checkDuplicatedUser(email: String) {
        val duplicatedUser = userRepository.findUserByEmail(email)

        if (duplicatedUser != null) {
            throw UserException(UserErrorMessage.DUPLICATED_EMAIL)
        }
    }

    fun validPassword(inputPassword: String, userPassword: String) {
        val isMatched = PasswordUtil.match(inputPassword, userPassword)

        if (!isMatched) {
            throw UserException(UserErrorMessage.NOT_FOUND_USER)
        }
    }
}
