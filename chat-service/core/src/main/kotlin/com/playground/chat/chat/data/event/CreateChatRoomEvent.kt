package com.playground.chat.chat.data.event

data class CreateChatRoomEvent(
    val userId: Long,
    val roomId: Long,
    val roomName: String
)
