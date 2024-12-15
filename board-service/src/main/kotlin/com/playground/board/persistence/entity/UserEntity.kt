package com.playground.board.persistence.entity

import com.playground.board.core.domain.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "user")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var email: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false)
    var createdTime: LocalDateTime = LocalDateTime.now(),

    var updatedTime: LocalDateTime? = null
) {
    constructor(): this(null, "", "", LocalDateTime.now(), null)

    fun toDomain(): User {
        return User(
            id = id,
            email = email,
            password = password,
            createdTime = createdTime,
            updatedTime = updatedTime
        )
    }
}
