package com.playground.chat.chat.data.event

data class LeaveChatRoomEvent(
    val userId: Long,
    val roomId: Long
)
