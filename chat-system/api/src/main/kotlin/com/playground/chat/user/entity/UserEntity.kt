package com.playground.chat.user.entity

import com.playground.chat.chat.entity.ChatEntity
import com.playground.chat.chat.entity.ChatRoomEntity
import com.playground.chat.global.auth.PrincipalRole
import com.playground.chat.global.entity.AuditEntity
import com.playground.chat.global.entity.IdGenerator
import jakarta.persistence.*
import org.hibernate.annotations.Filter
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.UuidGenerator
import java.util.*

@Entity
@Table(name = "user")
@Filter(name = "softDeleteFilter", condition = "is_deleted = :isDeleted")
@SQLDelete(sql = "UPDATE user u SET u.is_deleted = true WHERE u.id = ?")
class UserEntity(
    @Id
    @UuidGenerator(algorithm = IdGenerator::class)
    var id: UUID? = null,

    var email: String,

    var password: String,

    var image: String? = null,

    var nickname: String,

    @Enumerated(EnumType.STRING)
    var role: PrincipalRole,

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
