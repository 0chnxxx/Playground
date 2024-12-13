package com.playground.board.persistence

import com.playground.board.core.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository: JpaRepository<User, Long> {
}
