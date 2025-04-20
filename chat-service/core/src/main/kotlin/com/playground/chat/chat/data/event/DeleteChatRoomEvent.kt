package com.playground.chat.chat.data.event

data class DeleteChatRoomEvent(
    val userId: Long,
    val roomId: Long
)
