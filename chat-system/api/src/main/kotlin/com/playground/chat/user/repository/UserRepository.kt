package com.playground.chat.user.repository

import com.playground.chat.user.entity.QUserEntity
import com.playground.chat.user.entity.UserEntity
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class UserRepository(
    private val entityManager: EntityManager,
    private val jpaQueryFactory: JPAQueryFactory
) {
    fun findUserById(userId: UUID): UserEntity? {
        val qUser = QUserEntity("user")

        return jpaQueryFactory
            .selectFrom(qUser)
            .where(qUser.id.eq(userId))
            .fetchOne()
    }

    fun findUserByEmail(email: String): UserEntity? {
        val qUser = QUserEntity("user")

        return jpaQueryFactory
            .selectFrom(qUser)
            .where(qUser.email.eq(email))
            .fetchOne()
    }

    fun saveUser(user: UserEntity) {
        entityManager.persist(user)
    }
}
