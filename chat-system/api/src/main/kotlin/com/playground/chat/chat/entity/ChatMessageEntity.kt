package com.playground.chat.chat.entity

import com.playground.chat.global.entity.AuditEntity
import com.playground.chat.global.entity.IdGenerator
import com.playground.chat.user.entity.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "chat_message")
class ChatMessageEntity(
    @Id
    var id: UUID,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    var room: ChatRoomEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    var sender: UserEntity,

    var content: String,
): AuditEntity()
