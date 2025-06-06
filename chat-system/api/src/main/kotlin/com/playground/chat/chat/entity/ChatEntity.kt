package com.playground.chat.chat.entity

import com.playground.chat.global.entity.AuditEntity
import com.playground.chat.user.entity.UserEntity
import jakarta.persistence.*
import java.io.Serializable
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "chat")
@IdClass(ChatEntity.ChatId::class)
class ChatEntity(
    @Id
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id")
    var user: UserEntity,

    @Id
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "room_id")
    var room: ChatRoomEntity,

    @Column(name = "last_message_id")
    var lastMessageId: UUID? = null,

    @Column(name = "last_read_at")
    var lastReadAt: Instant? = null,

    @Column(name = "joined_at")
    var joinedAt: Instant = Instant.now()
): AuditEntity() {
    @Embeddable
    data class ChatId(
        val user: UUID,
        val room: UUID
    ): Serializable {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is ChatId) return false

            return user == other.user && room == other.room
        }

        override fun hashCode(): Int {
            return 31 * user.hashCode() + room.hashCode()
        }
    }

    fun read(messageId: UUID) {
        this.lastMessageId = messageId
        this.lastReadAt = Instant.now()
    }
}
