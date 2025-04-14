package com.playground.chat.user.entity

import com.playground.chat.chat.entity.ChatEntity
import com.playground.chat.chat.entity.RoomEntity
import jakarta.persistence.*

@Entity
@Table(name = "user")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val email: String,

    val password: String,

    val nickname: String,

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val chats: MutableList<ChatEntity> = mutableListOf(),

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "chat",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "room_id")]
    )
    val rooms: MutableList<RoomEntity> = mutableListOf()
)
