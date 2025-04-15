package com.playground.chat.chat.repository

import com.playground.chat.chat.entity.ChatEntity
import com.playground.chat.chat.entity.RoomEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ChatRepository: JpaRepository<ChatEntity, Long> {
    @Query("""
        SELECT c.room
        FROM ChatEntity c
        WHERE c.user.id = :userId
    """)
    fun findRoomByUserId(@Param("userId") userId: Long): List<RoomEntity>
}
