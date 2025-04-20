package com.playground.chat.chat.data.event

data class ChatRoomEvent(
    val type: Type,
    val userId: Long,
    val roomId: Long,
    val roomName: String
) {
    enum class Type {
        CREATE, JOIN, LEAVE, DELETE
    }
}
