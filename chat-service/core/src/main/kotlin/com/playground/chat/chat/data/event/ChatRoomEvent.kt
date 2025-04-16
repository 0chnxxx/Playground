package com.playground.chat.chat.data.event

data class ChatRoomEvent(
    val type: EventType,
    val userId: Long,
    val roomId: Long,
    val roomName: String
) {
    enum class EventType {
        CREATE, JOIN, LEAVE, DELETE
    }
}
