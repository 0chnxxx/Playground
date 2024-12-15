package com.playground.board.persistence

import com.playground.board.persistence.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository: JpaRepository<UserEntity, Long> {
    fun findByEmail(email: String): UserEntity?
}
