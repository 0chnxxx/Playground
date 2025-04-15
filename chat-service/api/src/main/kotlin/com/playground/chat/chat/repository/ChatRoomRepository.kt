package com.playground.chat.chat.repository

import com.playground.chat.chat.entity.ChatRoomEntity
import com.playground.chat.user.entity.UserEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ChatRoomRepository: JpaRepository<ChatRoomEntity, Long> {
    @Query("""
        SELECT c.room
        FROM ChatEntity c
        WHERE c.user = :user
    """)
    fun findAllByUser(@Param("user") user: UserEntity): List<ChatRoomEntity>
}
