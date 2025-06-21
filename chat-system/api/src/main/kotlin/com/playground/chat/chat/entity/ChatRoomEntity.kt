package com.playground.chat.chat.entity

import com.playground.chat.chat.domain.ChatRoom
import com.playground.chat.global.entity.AuditEntity
import com.playground.chat.global.entity.IdGenerator
import com.playground.chat.user.entity.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.Filter
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.UuidGenerator
import java.util.*

@Entity
@Table(name = "chat_room")
@Filter(name = "softDeleteFilter", condition = "is_deleted = :isDeleted")
@SQLDelete(sql = "UPDATE chat_room r SET r.is_deleted = true WHERE r.id = ?")
class ChatRoomEntity(
    @Id
    @UuidGenerator(algorithm = IdGenerator::class)
    var id: UUID? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    var owner: UserEntity,

    var image: String? = null,

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
): AuditEntity() {
    companion object {
        fun fromRoom(room: ChatRoom): ChatRoomEntity {
            return ChatRoomEntity(
                id = room.id,
                owner = UserEntity.fromUser(room.owner),
                image = room.image,
                name = room.name
            )
        }
    }

    fun toRoom(): ChatRoom {
        return ChatRoom(
            id = id,
            owner = owner.toUser(),
            image = image,
            name = name,
        )
    }
}
