package com.playground.chat.chat.repository

import com.playground.chat.chat.entity.ChatMessageEntity
import com.playground.chat.chat.entity.ChatRoomEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ChatMessageRepository: JpaRepository<ChatMessageEntity, Long> {
    @Query("""
        SELECT m
        FROM ChatMessageEntity m
        JOIN m.room r
        WHERE r = :room
        ORDER BY m.createdAt DESC
    """)
    fun findAllByRoom(room: ChatRoomEntity, pageable: Pageable): Page<ChatMessageEntity>
}
