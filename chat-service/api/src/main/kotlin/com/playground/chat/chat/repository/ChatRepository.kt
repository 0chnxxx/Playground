package com.playground.chat.chat.repository

import com.playground.chat.chat.entity.ChatEntity
import com.playground.chat.chat.entity.ChatRoomEntity
import com.playground.chat.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ChatRepository: JpaRepository<ChatEntity, Long> {
    @Query("""
        DELETE ChatEntity c
        WHERE c.room = :room
    """)
    fun deleteByRoom(@Param("room") room: ChatRoomEntity)

    @Query("""
        DELETE ChatEntity c
        WHERE c.user = :user AND c.room = :room
    """)
    fun deleteByUserAndRoom(@Param("user") user: UserEntity, @Param("room") room: ChatRoomEntity)
}
