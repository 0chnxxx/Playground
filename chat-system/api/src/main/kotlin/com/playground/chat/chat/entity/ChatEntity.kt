package com.playground.chat.chat.entity

import com.playground.chat.chat.domain.Chat
import com.playground.chat.global.entity.AuditEntity
import com.playground.chat.user.entity.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.Filter
import org.hibernate.annotations.SQLDelete
import java.io.Serializable
import java.time.Instant
import java.util.*

@Entity
@Table(name = "chat")
@IdClass(ChatEntity.ChatId::class)
@Filter(name = "softDeleteFilter", condition = "is_deleted = :isDeleted")
@SQLDelete(sql = "UPDATE chat c SET c.is_deleted = true WHERE c.room_id = ? AND c.user_id = ?")
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

    fun toChat(): Chat {
        return Chat(
            user = user.toUser(),
            room = room.toRoom(),
            lastMessageId = lastMessageId,
            lastReadAt = lastReadAt,
            joinedAt = joinedAt
        )
    }
}
