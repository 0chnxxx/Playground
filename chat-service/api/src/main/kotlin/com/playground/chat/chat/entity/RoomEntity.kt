package com.playground.chat.chat.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "room")
class RoomEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    val name: String? = null,

    @OneToMany(mappedBy = "room", cascade = [CascadeType.ALL], orphanRemoval = true)
    val chats: MutableList<ChatEntity> = mutableListOf(),

    @OneToMany(mappedBy = "room", cascade = [CascadeType.ALL], orphanRemoval = true)
    val messages: MutableList<MessageEntity> = mutableListOf(),

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)
