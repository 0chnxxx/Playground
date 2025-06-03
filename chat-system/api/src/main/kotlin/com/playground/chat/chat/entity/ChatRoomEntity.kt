package com.playground.chat.chat.entity

import com.playground.chat.user.entity.UserEntity
import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "chat_room")
class ChatRoomEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    var owner: UserEntity,

    var name: String,

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var chats: MutableList<ChatEntity> = mutableListOf(),

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "chat",
        joinColumns = [JoinColumn(name = "room_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    var users: MutableList<UserEntity> = mutableListOf(),

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var messages: MutableList<ChatMessageEntity> = mutableListOf(),

    @Column(name = "created_at")
    var createdAt: Instant = Instant.now()
)
