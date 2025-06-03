package com.playground.chat.chat.data.event

import java.util.UUID

data class ChatRoomEvent(
    val type: Type,
    val userId: UUID,
    val roomId: UUID,
    val roomName: String
) {
    enum class Type {
        CREATE, JOIN, LEAVE, DELETE
    }
}
