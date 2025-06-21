package com.playground.chat.user.entity

import com.playground.chat.chat.entity.ChatEntity
import com.playground.chat.chat.entity.ChatRoomEntity
import com.playground.chat.global.auth.PrincipalRole
import com.playground.chat.global.entity.AuditEntity
import com.playground.chat.global.entity.IdGenerator
import com.playground.chat.user.domain.User
import jakarta.persistence.*
import org.hibernate.Hibernate
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
    companion object {
        fun fromUser(user: User): UserEntity {
            return UserEntity(
                id = user.id,
                email = user.email,
                password = user.password,
                image = user.image,
                nickname = user.nickname,
                role = user.role,
                rooms = if (user.rooms.isNotEmpty()) {
                    user.rooms.map { ChatRoomEntity.fromRoom(it) }.toMutableList()
                } else {
                    mutableListOf()
                }
            )
        }
    }

    fun toUser(): User {
        return User(
            id = id,
            email = email,
            password = password,
            image = image,
            nickname = nickname,
            role = role,
            rooms = if (Hibernate.isInitialized(rooms)) {
                rooms.map { it.toRoom() }.toMutableList()
            } else {
                mutableListOf()
            }
        )
    }
}
