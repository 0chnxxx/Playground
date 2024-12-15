package com.playground.board.persistence.repository

import com.playground.board.persistence.UserJpaRepository
import com.playground.board.persistence.entity.UserEntity
import org.springframework.stereotype.Repository

@Repository
class UserRepository(
    private val userJpaRepository: UserJpaRepository
) {
    fun save(user: UserEntity) {
        userJpaRepository.save(user)
    }

    fun findByEmail(email: String): UserEntity? {
        return userJpaRepository.findByEmail(email)
    }
}
