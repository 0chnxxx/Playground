package com.playground.chat.chat.entity

import com.playground.chat.global.entity.AuditEntity
import com.playground.chat.user.entity.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.Filter
import org.hibernate.annotations.SQLDelete
import java.util.*

@Entity
@Table(name = "chat_message")
@Filter(name = "softDeleteFilter", condition = "is_deleted = :isDeleted")
@SQLDelete(sql = "UPDATE chat_message m SET m.is_deleted = true WHERE m.id = ?")
class ChatMessageEntity(
    @Id
    var id: UUID,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    var room: ChatRoomEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = true)
    var sender: UserEntity?,

    @Enumerated(EnumType.STRING)
    var type: ChatMessageType,

    var content: String,
): AuditEntity()
