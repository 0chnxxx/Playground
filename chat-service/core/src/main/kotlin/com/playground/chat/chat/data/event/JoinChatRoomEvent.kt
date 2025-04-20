package com.playground.chat.chat.data.event

data class JoinChatRoomEvent(
    val userId: Long,
    val roomId: Long
)
