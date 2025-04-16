package com.playground.chat.chat.entity

import com.playground.chat.user.entity.UserEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "chat_room")
class ChatRoomEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    val owner: UserEntity,

    val name: String,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "chat",
        joinColumns = [JoinColumn(name = "room_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    val users: MutableList<UserEntity> = mutableListOf(),

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val messages: MutableList<ChatMessageEntity> = mutableListOf(),

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)
