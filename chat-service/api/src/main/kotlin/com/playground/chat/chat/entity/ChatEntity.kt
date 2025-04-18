package com.playground.chat.chat.entity

import com.playground.chat.user.entity.UserEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "chat",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "room_id"])]
)
class ChatEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id")
    val user: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "room_id")
    val room: ChatRoomEntity,

    @Column(name = "joined_at")
    val joinedAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "last_read_at")
    var lastReadAt: LocalDateTime? = null
)
