package com.playground.chat.user.entity

import com.playground.chat.chat.entity.ChatEntity
import com.playground.chat.chat.entity.ChatRoomEntity
import com.playground.chat.global.entity.AuditEntity
import com.playground.chat.global.entity.IdGenerator
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.util.UUID

@Entity
@Table(name = "user")
class UserEntity(
    @Id
    @UuidGenerator(algorithm = IdGenerator::class)
    var id: UUID? = null,

    var email: String,

    var password: String,

    var nickname: String,

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    var chats: MutableList<ChatEntity> = mutableListOf(),

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "chat",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "room_id")]
    )
    var rooms: MutableList<ChatRoomEntity> = mutableListOf()
): AuditEntity() {
    fun isOwner(room: ChatRoomEntity): Boolean {
        return this.rooms.any { it == room && it.owner == this }
    }
}
