package com.playground.chat.user.entity

import com.playground.chat.chat.entity.ChatEntity
import com.playground.chat.chat.entity.ChatRoomEntity
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "user")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
) {
    fun isOwner(room: ChatRoomEntity): Boolean {
        return this.rooms.any { it == room && it.owner == this }
    }
}
