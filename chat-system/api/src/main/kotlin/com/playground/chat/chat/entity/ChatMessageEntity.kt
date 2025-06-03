package com.playground.chat.chat.entity

import com.playground.chat.user.entity.UserEntity
import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "chat_message")
class ChatMessageEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    var room: ChatRoomEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    var sender: UserEntity,

    var content: String,

    @Column(name = "created_at")
    var createdAt: Instant = Instant.now()
)
