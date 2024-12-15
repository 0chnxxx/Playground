package com.playground.board.core.domain

import com.playground.board.persistence.entity.UserEntity
import java.time.LocalDateTime

class User(
    val id: Long?,
    var email: String,
    var password: String,
    var createdTime: LocalDateTime,
    var updatedTime: LocalDateTime?
) {
    fun toEntity(): UserEntity {
        return UserEntity(
            id = id,
            email = email,
            password = password,
            createdTime = createdTime,
            updatedTime = updatedTime
        )
    }
}
